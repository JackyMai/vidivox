package vidivox.filechooser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vidivox.gui.VidivoxGUI;

/**
 * This class extends the FileChooser class and is designed to handling
 * file-opening operations for Vidivox.
 * 
 * Authors Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class FileOpener extends FileChooser {
	/**
	 * Shows a JFileChooser which will accept either a video or an audio file
	 * depending on the fileType specified.
	 * 
	 * @param mainFrame - the parent component to appear next to
	 * @param fileType - string which indicates what fileType is to be expected
	 * @return true if the file chosen by the user is valid
	 */
	private static boolean openMedia(JFrame mainFrame, String fileType) {
		// Resets the existing filters and disables the "All File" filter
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File(""));
		
		// Sets the filter depending on whether it is opening a video or audio file
		if(fileType.equals("video")) {
			setVideoOpenFilter();
		} else {
			setAudioOpenFilter();
		}
		
		int returnValue = fileChooser.showOpenDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			
			// Only proceed if the file name typed by the user in the file chooser
			// really does exist, show a JOptionPane if it doesn't exist
			if (chosenFile.exists() && chosenFile != null) {
				
				// Updates the chosen video/audio to the newly accepted file
				if(fileType.equals("video")) {
					VidivoxGUI.vm.setChosenVideo(chosenFile);
					VidivoxGUI.vp.playVideo(chosenFile);
				} else {
					VidivoxGUI.vm.setChosenAudio(chosenFile);
				}
				
				return true;
			} else {
				JOptionPane.showMessageDialog(mainFrame, 
						"The " + fileType + "file \"" + chosenFile.getName() + "\" does not exist!", 
						"Oops", 
						JOptionPane.WARNING_MESSAGE);
			}
		}
		
		return false;
	}
	
	/**
	 * Tells the openMedia method to open a open file specifically
	 */
	public static boolean openVideo(JFrame mainFrame) {
		return openMedia(mainFrame, "video");
	}
	
	/**
	 * Tells the openMedia method to open an audio file specifically
	 */
	public static boolean openAudio(JFrame mainFrame) {
		return openMedia(mainFrame, "audio");
	}
 }
