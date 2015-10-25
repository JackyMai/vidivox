package vidivox.model;

import java.io.File;
import java.util.ArrayList;

/**
 * This class contains the main model information for the Vidivox program.
 * This includes information such as the chosen video and the audio tracks
 * added by the user which will be used for the export operation.
 * 
 * Authors: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class VidivoxModel {
	private File chosenVideo;
	private File chosenAudio;
	private ArrayList<AudioTrack> audioList;
	private String[] delayedAudioList;
	
	/**
	 * Constructor for the VidivoxModel class which instantiates the ArrayList
	 * for saving the audio tracks.
	 */
	public VidivoxModel() {
		audioList = new ArrayList<AudioTrack>();
	}
	
	/**
	 * Returns the File object of the chosen video.
	 * @return the File object of the video chosen by the user
	 */
	public File getChosenVideo() {
		return chosenVideo;
	}
	
	/**
	 * Returns the absolute pathname string of the chosenVideo file.
	 * @return The absolute pathname string denoting the same file or directory of the chosen video
	 */
	public String getChosenVideoPath() {
		return chosenVideo.getAbsolutePath();
	}
	
	/**
	 * Sets the chosenVideo File object as the new File object specified by the input
	 * @param chosenVideo - new video file chosen by the user
	 */
	public void setChosenVideo(File chosenVideo) {
		this.chosenVideo = chosenVideo;
	}
	
	/**
	 * Sets the path of the temporary video file (for overwriting the current chosen video)
	 * depending on the parent path of the chosen video and renames the current chosen
	 * video to vidivoxTemp.
	 */
	public void setChosenVideoTemp() {
		File vidiTemp = new File(chosenVideo.getParent() + File.separator +"vidivoxTemp.avi");
		chosenVideo.renameTo(vidiTemp);
		setChosenVideo(vidiTemp);
	}
	
	/**
	 * Returns the File object of the latest chosen audio track.
	 * @return the File object of the latest audio chosen by the user
	 */
	public File getChosenAudio() {
		return chosenAudio;
	}
	
	/**
	 * Sets the chosenAudio File object as the new File object specified by the input
	 * @param chosenVideo - new video file chosen by the user
	 */
	public void setChosenAudio(File chosenAudio) {
		this.chosenAudio = chosenAudio;
	}
	
	/**
	 * Returns the ArrayList that stores audio tracks added by the user.
	 * @return the ArrayList of the audio tracks added by the user
	 */
	public ArrayList<AudioTrack> getAudioList() {
		return audioList;
	}
	
	/**
	 * Returns the number of elements in the ArrayList which stores all of the AudioTrack objects.
	 * @return the size of the ArrayList which holds all the AudioTrack objects
	 */
	public int getAudioListSize() {
		return audioList.size();
	}
	
	/**
	 * Appends a new AudioTrack object to the end of the audioList ArrayList
	 * @param newAudio - the new AudioTrack object that will be added to the ArrayList
	 */
	public void addAudioTrack(AudioTrack newAudio) {
		audioList.add(newAudio);
	}
	
	/**
	 * Sets the insert time of the specified audio track object in the ArrayList
	 * @param index - the index position of the AudioTrack object
	 * @param newTime - the new insert time for the AudioTrack object
	 */
	public void setInsertTime(int index, int newTime) {
		audioList.get(index).setInsertTime(newTime);
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 * Shifts any subsequent elements to the left (subtracts one from their indices).
	 * @param index - the index of the element to be removed
	 */
	public void removeAudioTrack(int index) {
		audioList.remove(index);
	}
	
	/**
	 * Returns the string array which contains all of the absolute file path for the
	 * delayed audio files.
	 * @return the string array which contains all of the absolute file path for the
	 * delayed audio files.
	 */
	public String[] getDelayedAudioList() {
		return delayedAudioList;
	}
	
	/**
	 * Sets the delayedAudioList with the new string array provided in the input argument
	 * @param newList - the new string array with the absolute path of all the delayed audio files
	 */
	public void setDelayedAudioList(String[] newList) {
		this.delayedAudioList = newList;
	}
}