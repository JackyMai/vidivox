package vidivox.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

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
	public static void festival(String text) {
		FestivalWorker fw = new FestivalWorker(text);
		fw.execute();
	}
	
	public static void export(final JFrame mainFrame, final String videoPath, final ArrayList<AudioTrack> audioList, final File desiredName) {
		DelayWorker dw = new DelayWorker(audioList);
		dw.execute();
		dw.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if("state" == e.getPropertyName() && SwingWorker.StateValue.DONE == e.getNewValue()) {
					ExportDialog exportDialog = new ExportDialog(mainFrame, videoPath, desiredName);
					exportDialog.setVisible(true);
				}
			}
		});
	}
}
