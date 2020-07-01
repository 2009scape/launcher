package com.fox.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.fox.Launcher;
import com.fox.Settings;
import com.fox.components.AppFrame;
import com.fox.net.CacheGrab;
import com.fox.net.Download;
import com.fox.net.Update;
import com.fox.utils.Utils;

public class ButtonListener implements ActionListener {
	
	public static Download download;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		
		case "_":
			Launcher.app.setState(JFrame.ICONIFIED);
			break;
			
		case "X":
			System.exit(0);
			break;
			
		case "play":
			Thread t = new Thread() {
				public void run() {
					
					AppFrame.playButton.setEnabled(false);
					AppFrame.pbar.setString("Checking for Client Updates...");
					
					byte status = Update.updateExists(0,null);
					byte idx255status = Update.updateExists(1,"main_file_cache.idx255");
					if (status == 0 && idx255status == 0) {
						AppFrame.pbar.setString("Now Launching "+Settings.SERVER_NAME+"!");
						Utils.launchClient();
						return;
					} else {
						System.out.println("JAR STATUS: " + status);
						System.out.println("CACHE STATUS: " + idx255status);
					}
					if( idx255status == 1 || idx255status == 3 || idx255status == 2){
						System.out.println("CACHE OUT OF DATE! Downloading new cache...");
						new CacheGrab(Settings.CACHE_URL).download();
					}
					if (status == 1 || status == 3) {
							System.out.println("JAR OUT OF DATE! Downloading new jar...");
							download = new Download(Settings.DOWNLOAD_URL);
							download.download();
							return;
						} else {
							if (download.getStatus() == Download.COMPLETE)
								return;
							
							if (download.getStatus() == Download.DOWNLOADING) {
								download.pause();
							} else if (download.getStatus() == Download.PAUSED) {
								download.resume();
							}
						}
					}
				};
			t.start();
			break;
			
		default:
			System.out.println(e.getActionCommand());
			break;
			
		}
	}
	public static boolean checkRun(){
		byte status = Update.updateExists(0,null);
		byte idx255status = Update.updateExists(1,"main_file_cache.idx255");
		if (status == 0 && idx255status == 0) {
			return true;
		} else {
			return false;
		}
	}

}
