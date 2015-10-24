package vidivox.filechooser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vidivox.gui.VidivoxGUI;

public class FileOpener extends FileChooser {
	private static boolean openMedia(JFrame mainFrame, String fileType) {
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File(""));
		
		if(fileType.equals("video")) {
			setVideoOpenFilter();
		} else {
			setAudioOpenFilter();
		}
		
		int returnValue = fileChooser.showOpenDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			if (chosenFile.exists() && chosenFile != null) {
				if(fileType.equals("video")) {
					VidivoxGUI.vm.setChosenVideo(chosenFile);
					VidivoxGUI.vp.playVideo(chosenFile);
				} else {
					VidivoxGUI.vm.setChosenAudio(chosenFile);
				}
				
				return true;
			} else {
				JOptionPane.showMessageDialog(mainFrame,
					"The " + fileType + "file \"" + chosenFile.getName() + "\" does not exist!");
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
