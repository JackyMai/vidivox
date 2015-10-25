package vidivox.filechooser;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vidivox.gui.VidivoxGUI;
import vidivox.model.AudioTrack;
import vidivox.worker.VidivoxWorker;

public class FileSaver extends FileChooser {
	/**
	 * Shows a JFileChooser which will accept either a video or an audio file
	 * depending on the fileType specified, and will either save the message as
	 * an mp3 file or overlay the audio track into the selected video file.
	 * 
	 * @param mainFrame - the parent component for the prompt to appear at
	 * @param fileType - string which indicates what fileType is to be expected
	 */
	public static void exportFile(JFrame mainFrame, String fileType, String message) {
		// Resets the existing filters and disables the "All File" filter
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		// Sets the filter depending on whether it is opening a video or audio file
		// and suggests the default output file name
		if (fileType.equals("video")) {
			setVideoSaveFilter();
			fileChooser.setSelectedFile(new File("vidiMix.avi"));
		} else {
			setAudioSaveFilter();
			fileChooser.setSelectedFile(new File("vidiVoice.mp3"));
		}
		
		int returnValue = fileChooser.showSaveDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();
			
			// Checks whether the user has entered the suffix for the file type
			// they wish the file to save as
			desiredName = checkSuffix(desiredName, fileType);
			
			// If the file selected already exists, display a JOptionPane and ask
			// the user for overwriting confirmation
			boolean confirmSave = checkOverwrite(mainFrame, desiredName, fileType);
			
			// 
			if (confirmSave == true) {
				if (fileType.equals("video")) {
					String videoPath = VidivoxGUI.vm.getChosenVideoPath();
					ArrayList<AudioTrack> audioList = VidivoxGUI.vm.getAudioList();
					
					VidivoxWorker.export(mainFrame, videoPath, audioList, desiredName);
				} else {
					VidivoxWorker.saveMp3File(message, desiredName);
				}
			}
		}
	}
	
	/**
	 * Checks the suffix of the entered file name, if it does not contain the .avi
	 * or .mp3 extension at the end then add it on.
	 * @param desiredName - the desired file name entered by the user
	 * @param fileType - string to indicate the filter type, either "video" or "audio"
	 * @return the desiredName with the video or audio extension attached
	 */
	private static File checkSuffix(File desiredName, String fileType) {
		if(fileType.equals("video")) {
			if(!desiredName.getName().endsWith(".avi")) {
				desiredName = new File(desiredName.getAbsolutePath() + ".avi");
			}
		} else {
			if(!desiredName.getName().endsWith(".mp3")) {
				desiredName = new File(desiredName.getAbsolutePath() + ".mp3");
			}
		}
		
		return desiredName;
	}
		
	/**
	 * Checks for the possibility of overwriting any files with the same path and
	 * shows a JOptionPane to the user for confirmation before overwriting the files.
	 * For the video export operation there is a possibility of overwriting the
	 * chosen video itself (overwriting the current chosen video that you wish
	 * the exported video to have the same name) so extra precaution has been taken.
	 * 
	 * @param mainFrame - the parent component for the prompt to appear at
	 * @param desiredName - the file object that the user wishes to save as
	 * @param fileType - a string that indicates the file type, either "video" or "audio"
	 * @return true for overwrite confirmation from the user.
	 */
	private static boolean checkOverwrite(JFrame mainFrame, File desiredName, String fileType) {
		// For the video export operation there is a possibility of overwriting the
		// chosen video itself (overwriting the current chosen video that you wish
		// the exported video to have the same name) so extra care is required.
		if(fileType.equals("video")) {
			String chosenVideoPath = VidivoxGUI.vm.getChosenVideoPath();
			
			// If absolute path of the current video matches the absolute path of the
			// new video file that the user wishes to save as, ask for confirmation.
			if(desiredName.getAbsolutePath().equals(chosenVideoPath)) {
				int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
						"You are overwriting the original video file. \nAre you sure you wish to overwrite it?",
						"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				// Rename the original file to a temporary file then use it to overlay the video instead
				if(overwriteReponse == JOptionPane.YES_OPTION) {
					VidivoxGUI.vm.setChosenVideoTemp();
					
					return true;
				}
			}
		}
		
		// General overwriting warning for both video and audio file type.
		if (desiredName.exists() && desiredName != null) {
			int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
					"The " + fileType + " file \"" + desiredName.getName()
							+ "\" already exists. Do you wish to overwrite it?",
					"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			// If the user clicks yes, delete the existing file with the same name
			// and set confirmSave to true
			if (overwriteReponse == JOptionPane.YES_OPTION) {
				desiredName.delete();
				return true;
			}
		} else {
			return true;
		}
		
		return false;
	}
}
