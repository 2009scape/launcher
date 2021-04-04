package com.fox;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class Settings {

	public static final String SERVER_NAME = "2009scape";
	public static final String DOWNLOAD_URL = "http://play.2009scape.org/2009scape.jar";
	public static final String CACHE_URL = "http://play.2009scape.org/";

	public static final String SAVE_NAME = "2009scape.jar";
	public static final String SAVE_DIR = System.getProperty("user.home") + File.separator;
	public static final String CACHE_DIR = System.getProperty("user.home") + File.separator + ".runite_rs" + File.separator + "runescape" + File.separator;
	
	public static final String SERVER_IP = "play.2009scape.org";
	public static final int SERVER_PORT = 43595;
	
	public static final boolean enableMusicPlayer = false;
	
	// Frame Settings
	public static final Dimension frameSize = new Dimension(600, 350);
	public static final Color borderColor = new Color(0, 0, 0);
	public static final Color backgroundColor = new Color(158,134,94);
	public static final Color primaryColor = new Color(255,204,54);
	public static final Color iconShadow = new Color(0, 0, 0);
	public static final Color buttonDefaultColor = new Color(
			97,76,35
	);
	
	// link settings
	//public static final String youtube = "";
	//public static final String twitter = "";
	//public static final String facebook = "";
	
	public static final String community = "https://discord.gg/UVtqkDhxVD";
	public static final String leaders = "https://2009scape.org/services/m%3dhiscore/hiscores.html";
	//public static final String store = "";
	//public static final String vote = "";
	public static final String bugs = "https://2009scape.org/services/m=bugtracker_v4/index.html?j";
	
}
