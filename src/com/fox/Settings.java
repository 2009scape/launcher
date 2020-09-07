package com.fox;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class Settings {

	public static final String SERVER_NAME = "2009scape";
	public static final String DOWNLOAD_URL = "http://127.0.0.1/2009scape.jar";
	public static final String CACHE_URL = "http://127.0.0.1/";

	public static final String SAVE_NAME = "2009scape.jar";
	public static final String SAVE_DIR = System.getProperty("user.home") + File.separator;
	public static final String CACHE_DIR = System.getProperty("user.home") + File.separator + ".runite_rs" + File.separator + "runescape" + File.separator;
	
	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 43595;
	
	public static final boolean enableMusicPlayer = true;
	
	// Frame Settings
	public static final Dimension frameSize = new Dimension(600, 350);
	public static final Color borderColor = new Color(0, 0, 0);
	public static final Color backgroundColor = new Color(30, 30, 30);
	public static final Color primaryColor = new Color(59, 166, 226);
	public static final Color iconShadow = new Color(0, 0, 0);
	public static final Color buttonDefaultColor = new Color(255, 255, 255);
	
	// link settings
	//public static final String youtube = "";
	//public static final String twitter = "";
	//public static final String facebook = "";
	
	public static final String community = "";
	public static final String leaders = "";
	//public static final String store = "";
	//public static final String vote = "";
	public static final String bugs = "https://github.com/2009scape/2009Scape/issues";
	
}
