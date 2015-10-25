package vidivox.model;

import java.io.File;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import vidivox.helper.TimeFormatter;

/*
 * This class deals with the player/playerComponent and logic associated with the player. 
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

	public void play() {
		videoPaused = 0;
		player.mute(false);
		player.play();
	}

	public void pause() {
		videoPaused = 1;
		player.pause();
	}

	public void stop() {
		videoPaused = 1;
		player.stop();
	}
	
	public void skip(int direction) {
		videoPaused = 2;
		skipDirection = direction;
		player.mute(true);
	}

	public void playVideo(File chosenVideo) {
		videoPaused = 0;
		player.playMedia(chosenVideo.getAbsolutePath());
	}

	
	// ----------------------- Getters and setters -----------------------
	// Getters are public, setters are package visibility

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
