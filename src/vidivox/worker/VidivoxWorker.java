package vidivox.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import vidivox.gui.ExportDialog;
import vidivox.model.AudioTrack;

/**
 * This class contains static methods used for the various SwingWorker
 * operations.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class VidivoxWorker {
	/**
	 * Creates a new SaveMp3Worker that will save the given text as
	 * an mp3 file at the desired path.
	 * @param message - a string that the user wants to convert to mp3
	 * @param desiredName - a File object which contains the output destination
	 */
	public static void saveMp3File(String message, File desiredName) {
		SaveMp3Worker smw = new SaveMp3Worker(message, desiredName);
		smw.execute();
	}
	
	/**
	 * Creates a new FestivalWorker that will say the text you have entered
	 * using the voice of Festival.
	 * @param text - a string containing the text you want Festival to speak
	 */
	public static FestivalWorker festival(String text, final JButton playButton) {
		final FestivalWorker fw = new FestivalWorker(text);
		fw.execute();
		fw.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				// If the FestivalWorker enters the done state then change the button text back to "Play"
				if("state" == e.getPropertyName() && SwingWorker.StateValue.DONE == e.getNewValue()) {
					playButton.setText("Play");
				}
			}
		});
		
		return fw;
	}
	
	/**
	 * Creates a JDialog which starts the export operation and informs the user
	 * that the operation is taking place.
	 * @param mainFrame - the parent component for the JDialog to appear at
	 * @param videoPath - the absolute path of the chosen video to merge with
	 * @param audioList - an array list containing all AudiOTrack objects that will be merged
	 * @param desiredName - a File containing the desired output name and destination
	 */
	public static void export(JFrame mainFrame, String videoPath, ArrayList<AudioTrack> audioList, File desiredName) {
		ExportDialog exportDialog = new ExportDialog(mainFrame, videoPath, audioList, desiredName);
		exportDialog.setVisible(true);
	}
}
