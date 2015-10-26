package vidivox.model;

import java.io.File;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import vidivox.helper.TimeFormatter;

/*
 * This class deals with the player/playerComponent and logic 
 * associated with the video player. 
 * 
 * Authors: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */

public class VidivoxPlayer {
	private final EmbeddedMediaPlayerComponent playerComponent;
	protected EmbeddedMediaPlayer player;
	private String videoLength = "00:00";
	private int videoDuration = 0;
	private int defaultVolume = 100;
	private static int videoPaused = 0;
	private int skipDirection = 0;

	// Constructor: Initialises a new player component and player object
	public VidivoxPlayer() {
		playerComponent = new EmbeddedMediaPlayerComponent();
		player = playerComponent.getMediaPlayer();
	}

	
	// ------------------- Video logic manipulation methods --------------------
	/**
	 * Tells the video player to start playing normally
	 */
	public void play() {
		videoPaused = 0;
		player.mute(false);
		player.play();
	}

	/**
	 * Tells the video player to pause
	 */
	public void pause() {
		videoPaused = 1;
		player.pause();
	}

	/**
	 * Tells the player to reset
	 */
	public void stop() {
		videoPaused = 1;
		player.stop();
	}
	
	/**
	 * Tells the player to start skipping in the direction specified
	 */
	public void skip(int direction) {
		videoPaused = 2;
		skipDirection = direction;
		player.mute(true);
	}

	/**
	 * Tells the player to play the chosen video file
	 */
	public void playVideo(File chosenVideo) {
		videoPaused = 0;
		player.playMedia(chosenVideo.getAbsolutePath());
	}

	
	// -------------------------- Getters and setters --------------------------
	// Generic getters and setters for the video player

	public EmbeddedMediaPlayerComponent getPlayerComponent() {
		return playerComponent;
	}
	
	public EmbeddedMediaPlayer getPlayer() {
		return player;
	}
	
	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}

	public int getVideoDuration() {
		return videoDuration;
	}
	
	public void setVideoDuration(int videoDuration) {
		this.videoDuration = videoDuration;
		setVideoLength(TimeFormatter.milliToString(videoDuration));
	}
	
	public long getCurrentTime() {
		return player.getTime();
	}
	
	public int getDefaultVolume() {
		return defaultVolume;
	}

	public void setDefaultVolume(int defaultVolume) {
		this.defaultVolume = defaultVolume;
	}
	
	public static int getVideoPaused() {
		return videoPaused;
	}

	public int getSkipDirection() {
		return skipDirection;
	}
}
