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
	 * exportVideo: This method takes a string of fileType (either "video"
	 * or "audio") and a string of festival message ("null" for video file) 
	 * and will either save the message as an mp3 file or overlay the audio
	 * track into the selected video file, through the JFileChooser.
	 */
	public static void exportFile(JFrame mainFrame, String fileType, String message) {
		// Set file filter depending on the file type input
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(new File(""));
		
		if (fileType.equals("video")) {
			setVideoSaveFilter();
		} else {
			setAudioSaveFilter();
		}
		
		int returnValue = fileChooser.showSaveDialog(mainFrame);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = fileChooser.getSelectedFile();
			
			desiredName = checkSuffix(desiredName, fileType);
			
			// If the file selected already exists, display a JOptionPane and ask
			// the user for overwriting confirmation
			boolean confirmSave = checkOverwrite(mainFrame, desiredName, fileType);
			
			if (confirmSave == true) {
				if (fileType.equals("video")) {
					String videoPath = VidivoxGUI.vm.getChosenVideoPath();
					ArrayList<AudioTrack> audioList = VidivoxGUI.vm.getAudioList();
					
					VidivoxWorker.export(videoPath, audioList, desiredName);
				} else {
					VidivoxWorker.saveMp3File(message, desiredName);
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
		if(fileType.equals("video")) {
			String chosenVideoPath = VidivoxGUI.vm.getChosenVideoPath();
			
			if(desiredName.getAbsolutePath().equals(chosenVideoPath)) {
				int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
						"You are overwriting the original video file. \nAre you sure you wish to overwrite it?",
						"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				// Rename the original file to a temporary file then use it to overlay the video
				if(overwriteReponse == JOptionPane.YES_OPTION) {
					VidivoxGUI.vm.setChosenVideoTemp();
					
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
				return true;
			}
		} else {
			return true;
		}
		
		return false;
	}
}
