package vidivox;

import java.io.File;

public class AudioTrack {
	private File audioFile;
	private int insertTime;
	
	public AudioTrack(File audioFile, int insertTime) {
		this.audioFile = audioFile;
		this.insertTime = insertTime;
	}
	
	public File getAudioFile() {
		return audioFile;
	}
	
	public String getAudioName() {
		return audioFile.getName();
	}
	
	public String getAudioPath() {
		return audioFile.getAbsolutePath();
	}
	
	public int getInsertTime() {
		return insertTime;
	}
}
