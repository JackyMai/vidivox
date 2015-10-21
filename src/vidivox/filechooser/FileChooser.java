package vidivox.filechooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
	protected static JFileChooser fileChooser = new JFileChooser();
	protected static FileNameExtensionFilter videoOpenFilter = new FileNameExtensionFilter("Video Files (mp4, avi)", "mp4", "avi");
	protected static FileNameExtensionFilter videoSaveFilter = new FileNameExtensionFilter("Video Files (avi)", "avi");
	protected static FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("MP3 Files", "mp3");
	
	protected static void setVideoOpenFilter() {
		fileChooser.setDialogTitle("Open video");
		fileChooser.setFileFilter(videoOpenFilter);
	}
	
	protected static void setVideoSaveFilter() {
		fileChooser.setDialogTitle("Choose where to save the overlayed video");
		fileChooser.setFileFilter(videoSaveFilter);
	}
	
	protected static void setAudioOpenFilter() {
		fileChooser.setDialogTitle("Select audio track to overlay");
		fileChooser.setFileFilter(audioFilter);
	}
	
	protected static void setAudioSaveFilter() {
		fileChooser.setDialogTitle("Choose where to save the festival audio track");
		fileChooser.setFileFilter(audioFilter);
	}
}
