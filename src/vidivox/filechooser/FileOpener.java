package vidivox.filechooser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import vidivox.VidivoxGUI;

public class FileOpener extends FileChooser {
	private static boolean openMedia(JFrame mainFrame, String fileType) {
		if(fileType.equals("video")) {
			setVideoOpenFilter();
		} else {
			setAudioOpenFilter();
		}
		
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File(""));
		
		int returnValue = fileChooser.showOpenDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();
			if (desiredName.exists() && desiredName != null) {
				if(fileType.equals("video")) {
					VidivoxGUI.vp.setChosenVideo(fileChooser.getSelectedFile());
					VidivoxGUI.vp.playVideo();
				} else {
					VidivoxGUI.vp.setChosenAudio(fileChooser.getSelectedFile());
				}
				
				return true;
			} else {
				JOptionPane.showMessageDialog(mainFrame,
					"The " + fileType + "file \"" + desiredName.getName() + "\" does not exist!");
			}
		}
		
		return false;
	}
	
	public static boolean openVideo(JFrame mainFrame) {
		return openMedia(mainFrame, "video");
	}
	
	public static boolean openAudio(JFrame mainFrame) {
		return openMedia(mainFrame, "audio");
	}
 }
