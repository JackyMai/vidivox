package vidivox;

import java.io.File;
import java.util.ArrayList;

public class VidivoxModel {
	private File chosenVideo;
	private File chosenAudio;
	private ArrayList<AudioTrack> audioList;
	private String[] delayedAudioList;
	
	public VidivoxModel() {
		audioList = new ArrayList<AudioTrack>();
	}
	
	public File getChosenVideo() {
		return chosenVideo;
	}
	
	public String getChosenVideoPath() {
		return chosenVideo.getAbsolutePath();
	}
	
	public void setChosenVideo(File chosenVideo) {
		this.chosenVideo = chosenVideo;
	}
	
	public void setChosenVideoTemp() {
		File vidiTemp = new File(chosenVideo.getParent() + File.separator +"vidivoxTemp.avi");
		chosenVideo.renameTo(vidiTemp);
		setChosenVideo(vidiTemp);
	}
	
	public File getChosenAudio() {
		return chosenAudio;
	}
	
	public void setChosenAudio(File chosenAudio) {
		this.chosenAudio = chosenAudio;
	}
	
	public ArrayList<AudioTrack> getAudioList() {
		return audioList;
	}
	
	public void addAudioTrack(AudioTrack newAudio) {
		audioList.add(newAudio);
	}
	
	public void removeAudioTrack(int index) {
		audioList.remove(index);
	}
	
	public String[] getDelayedAudioList() {
		return delayedAudioList;
	}
	
	public void setDelayedAudioList(String[] newList) {
		this.delayedAudioList = newList;
	}
}
