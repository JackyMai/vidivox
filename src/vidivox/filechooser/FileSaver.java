package vidivox.filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vidivox.VidivoxGUI;
import vidivox.VidivoxWorker;

public class FileSaver extends FileChooser {
	/**
	 * exportVideo: This method takes a string of fileType (either "video"
	 * or "audio") and a string of festival message ("null" for video file) 
	 * and will either save the message as an mp3 file or overlay the audio
	 * track into the selected video file, through the JFileChooser.
	 */
	public static void exportFile(JFrame mainFrame, String fileType, String message) {
		// Set file filter depending on the file type input
		if (fileType.equals("video")) {
			fileChooser.setDialogTitle("Choose where to save the overlayed video");
			fileChooser.setFileFilter(videoFilter);
		} else {
			fileChooser.setDialogTitle("Choose where to save the festival audio track");
			fileChooser.setFileFilter(audioFilter);
		}
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File(""));
		
		int returnValue = fileChooser.showSaveDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();
			
			desiredName = checkSuffix(desiredName, fileType);
			
			// If the file selected already exists, display a JOptionPane and ask
			// the user for overwriting confirmation
			boolean confirmSave = checkOverwrite(mainFrame, desiredName, fileType);
			
			if (confirmSave == true) {
				if (fileType.equals("video")) {
					String videoPath = VidivoxGUI.vp.getChosenVideo().getAbsolutePath();
					String audioPath = VidivoxGUI.vp.getChosenAudio().getAbsolutePath();
					
					VidivoxWorker.overlay(videoPath, audioPath, desiredName, VidivoxGUI.vp);
				} else {
					VidivoxWorker.saveMp3File(message, desiredName, VidivoxGUI.vp);
				}
			}
		}
	}
	
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
	
	private static boolean checkOverwrite(JFrame mainFrame, File desiredName, String fileType) {
		boolean confirmSave = false;
		
		if(fileType.equals("video")) {
			String chosenVideoPath = VidivoxGUI.vp.getChosenVideo().getAbsolutePath();
			
			if(desiredName.getAbsolutePath().equals(chosenVideoPath)) {
				int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
						"You are overwriting the original video file. \nAre you sure you wish to overwrite it?",
						"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				// Rename the original file to a temporary file then use it to overlay the video
				if(overwriteReponse == JOptionPane.YES_OPTION) {
					VidivoxGUI.vp.setChosenVideoTemp();
					
					return true;
				}
			}
		}
		
		if (desiredName.exists() && desiredName != null) {
			int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
					"The " + fileType + " file \"" + desiredName.getName()
							+ "\" already exists. Do you wish to overwrite it?",
					"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			// If the user clicks yes, delete the existing file with the same name
			// and set confirmSave to true
			if (overwriteReponse == JOptionPane.YES_OPTION) {
				desiredName.delete();
				confirmSave = true;
			}
		} else {
			confirmSave = true;
		}
		
		return confirmSave;
	}
}
