package vidivox.model;

import java.io.File;

/**
 * Contains the model information of an AudioTrack object.
 * This means it has both the absolute path of the audio file
 * and the insert time the user wishes the audio track to start at.
 * 
 * Authors Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioTrack {
	private File audioFile;
	private int insertTime;
	
	/**
	 * Generic constructor for the AudioTrack object.
	 * @param audioFile - the File object of the audio file
	 * @param insertTime - the insert time to play the audio file at
	 */
	public AudioTrack(File audioFile, int insertTime) {
		this.audioFile = audioFile;
		this.insertTime = insertTime;
	}
	
	/**
	 * Returns the audio file object inside the AudioTrack object.
	 * @return The audio file object inside the AudioTrack object
	 */
	public File getAudioFile() {
		return audioFile;
	}
	
	/**
	 * Returns the name of the audio file object.
	 * @return The name of the audio file object
	 */
	public String getAudioName() {
		return audioFile.getName();
	}
	
	/**
	 * Returns the absolute path of the audio file object.
	 * @return The absolute path of the audio file object
	 */
	public String getAudioPath() {
		return audioFile.getAbsolutePath();
	}
	
	/**
	 * Returns the integer insert time in milliseconds of the AudioTrack object.
	 * @returnthe Tnteger insert time in milliseconds of the AudioTrack object
	 */
	public int getInsertTime() {
		return insertTime;
	}
	
	/**
	 * Sets the insert time of the audio track file.
	 * @param newTime - the insert time of the audio track file
	 */
	public void setInsertTime(int newTime) {
		this.insertTime = newTime;
	}
}
