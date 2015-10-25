package vidivox.filechooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class contains a static JFileChooser object which will be shared
 * across both the file opening and saving operations of Vidivox.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class FileChooser {
	protected static JFileChooser fileChooser = new JFileChooser();
	protected static FileNameExtensionFilter videoOpenFilter = new FileNameExtensionFilter("Video Files (mp4, avi)", "mp4", "avi");
	protected static FileNameExtensionFilter videoSaveFilter = new FileNameExtensionFilter("Video Files (avi)", "avi");
	protected static FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("MP3 Files", "mp3");
	
	// -------------------------- JFileChooser Setting Methods --------------------------
	/**
	 * Sets the appropriate JFileChooser title for opening a video
	 * and applies the video opening filter.
	 */
	protected static void setVideoOpenFilter() {
		fileChooser.setDialogTitle("Open video");
		fileChooser.setFileFilter(videoOpenFilter);
	}
	
	/**
	 * Sets the appropriate JFileChooser title for saving a video
	 * and applies the video saving filter.
	 */
	protected static void setVideoSaveFilter() {
		fileChooser.setDialogTitle("Choose where to save the overlayed video");
		fileChooser.setFileFilter(videoSaveFilter);
	}
	
	/**
	 * Sets the appropriate JFileChooser title for opening an audio
	 * and applies the audio opening filter.
	 */
	protected static void setAudioOpenFilter() {
		fileChooser.setDialogTitle("Select audio track to overlay");
		fileChooser.setFileFilter(audioFilter);
	}
	
	/**
	 * Sets the appropriate JFileChooser title for saving an audio
	 * and applies the audio saving filter.
	 */
	protected static void setAudioSaveFilter() {
		fileChooser.setDialogTitle("Choose where to save the festival audio track");
		fileChooser.setFileFilter(audioFilter);
	}
}
