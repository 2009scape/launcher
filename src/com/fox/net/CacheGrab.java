package com.fox.net;

import com.fox.Settings;
import com.fox.components.AppFrame;
import com.fox.listeners.ButtonListener;
import com.fox.utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

public class CacheGrab extends Observable implements Runnable{
    private static final int MAX_BUFFER_SIZE = 1024;
    public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error" };
    private String DIR = Settings.CACHE_DIR;
    private String fileName;
    public boolean allComplete;

    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private URL url;
    private int size;
    private int downloaded;
    private int status;

    public CacheGrab(String url){
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url.toString();
    }

    public int getSize() {
        return size;
    }

    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    public int getStatus() {
        return status;
    }

    public void pause() {
        status = PAUSED;
    }

    public void resume() {
        status = DOWNLOADING;
        download();
    }

    public void cancel() {
        status = CANCELLED;
    }

    private void error() {
        status = ERROR;
    }

    public void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @SuppressWarnings("unused")
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    public void run() {
        byte dat0status = Update.updateExists(1, "main_file_cache.dat0");
        byte dat2status = Update.updateExists(1, "main_file_cache.dat2");
        if (dat0status == 1 || dat0status == 3) {
                try {
                    this.url = new URL(Settings.CACHE_URL + "main_file_cache.dat0");
                    this.fileName = "main_file_cache.dat0";
                    this.DIR = Settings.CACHE_DIR;
                    this.size = -1;
                    doDownload();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
        }
        if (dat2status == 1 || dat2status == 3) {
            try {
                this.url = new URL(Settings.CACHE_URL + "main_file_cache.dat2");
                this.fileName = "main_file_cache.dat2";
                this.DIR = Settings.CACHE_DIR;
                this.size = -1;
                doDownload();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }        }
        for (int i = 0; i <= 28; i++) {
            byte datstatus = Update.updateExists(1,"main_file_cache.idx" + i);
            if(datstatus == 1 || datstatus == 3) {
                try {
                    this.url = new URL(Settings.CACHE_URL + "main_file_cache.idx" + i);
                    this.fileName = "main_file_cache.idx" + i;
                    this.DIR = Settings.CACHE_DIR;
                    this.size = -1;
                    doDownload();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            this.url = new URL(Settings.CACHE_URL + "main_file_cache.idx255");
            this.fileName = "main_file_cache.idx255";
            this.DIR = Settings.CACHE_DIR;
            this.size = -1;
            doDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Finishing up!");
        status = COMPLETE;
        allComplete = true;
        if(ButtonListener.checkRun()){
            System.out.println("Done with cache :D");
            AppFrame.playButton.setEnabled(true);
            AppFrame.pbar.setValue(0);
            AppFrame.pbar.setString("Click Launch to play "+Settings.SERVER_NAME+"!");
            stateChanged();
        }
    }

    public void doDownload(){
        AppFrame.playButton.setEnabled(false);
        RandomAccessFile file = null;
        InputStream stream = null;
        status = DOWNLOADING;
        downloaded = 0;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();

            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            int contentLength = connection.getContentLength();

            if (contentLength < 1) {
                error();
            }

            if (size == -1) {
                size = contentLength;
            }

            if(!new File(Settings.CACHE_DIR).exists()){
                System.out.println(".runite-rs does not exist, creating it...");
                new File(Settings.CACHE_DIR).mkdirs();
            }
            file = new RandomAccessFile(DIR + fileName, "rw");
            file.seek(downloaded);

            stream = connection.getInputStream();

            int lastNum = 0;

            while (status == DOWNLOADING) {

                byte buffer[];

                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                int read = stream.read(buffer);

                if (read == -1)
                    break;

                int progress = (int) getProgress();

                if (progress > lastNum) {
                    AppFrame.pbar.setValue(progress);
                    lastNum = progress;
                    AppFrame.pbar.setString("Downloading Update: "+progress+"%");
                }

                file.write(buffer, 0, read);
                downloaded += read;
                if(contentLength == downloaded){
                    status = COMPLETE;
                }
            }
        } catch (Exception e) {
            error();
        } finally {
            if (file != null)
                try { file.close(); } catch (Exception e) { }
            if (stream != null)
                try { stream.close();  } catch (Exception e) { }
        }
    }
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
