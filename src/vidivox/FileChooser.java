package vidivox;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
	private static JFileChooser fileChooser = new JFileChooser();
	private static FileNameExtensionFilter videoFilter = new FileNameExtensionFilter("Video Files (mp4, avi)", "mp4", "avi");
	private static FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("MP3 Files", "mp3");
	
	public static boolean openVideo(JFrame mainFrame) {
		fileChooser.setDialogTitle("Open video");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(videoFilter);
		
		int returnValue = fileChooser.showOpenDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();
			if (desiredName.exists() && desiredName != null) {
				VidivoxGUI.vp.setChosenVideo(fileChooser.getSelectedFile());
				VidivoxGUI.vp.playVideo();
				
				return true;
			} else {
				JOptionPane.showMessageDialog(mainFrame,
					"The video file \"" + desiredName.getName() + "\" does not exist!");
			}
		}
		
		return false;
	}
	
	public static boolean openAudio(JFrame mainFrame) {
		fileChooser.setDialogTitle("Select audio track to overlay");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(audioFilter);
		
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();

			if (desiredName.exists() && desiredName != null) {
				VidivoxGUI.vp.setChosenAudio(fileChooser.getSelectedFile());
				
				return true;
			} else {
				JOptionPane.showMessageDialog(mainFrame,
						"The audio track \"" + desiredName.getName() + "\" does not exist!");
			}
		}
		
		return false;
	}
}
