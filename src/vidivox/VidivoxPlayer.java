package vidivox;

import java.io.File;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/*
 * This class deals with the player/playerComponent and logic associated with the player. 
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */

public class VidivoxPlayer {
	private final EmbeddedMediaPlayerComponent playerComponent;
	protected static EmbeddedMediaPlayer player;
	private File chosenVideo;
	private File chosenAudio;
	private String videoLength = "00:00";
	private int defaultVolume = 80;
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

	public void playVideo() {
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
	
	public File getChosenAudio() {
		return chosenAudio;
	}

	public void setChosenAudio(File chosenAudio) {
		this.chosenAudio = chosenAudio;
	}

	public File getChosenVideo() {
		return chosenVideo;
	}

	public void setChosenVideo(File chosenVideo) {
		this.chosenVideo = chosenVideo;
	}
	
	public void setChosenVideoTemp() {
		File vidiTemp = new File(chosenVideo.getParent() + File.separator +"vidiTemp_JH.avi");
		chosenVideo.renameTo(vidiTemp);
		setChosenVideo(vidiTemp);
	}
	
	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
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
