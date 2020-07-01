package com.fox.audio;

import javax.sound.midi.*;

public class Audio {
	
	private static Sequencer player;
	private String midiFile;
	
	public Audio(String file) {
		this.midiFile = file;
		try {
			player = MidiSystem.getSequencer();
			player.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void playAudio() {
		if (player == null || !player.isOpen() || player.isRunning())
			return;
		try {
			player.setSequence(getClass().getResourceAsStream(midiFile));
			player.setLoopCount(3);
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopAudio() {
		if (player == null)
			return;
		player.stop();
	}
	
	public static void main(String[] args) {
		Audio audio = new Audio("/data/audio/welcomehome.mid");
		audio.playAudio();
	}
}