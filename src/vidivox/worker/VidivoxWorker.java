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
 * 
 * @author jacky
 *
 */
public class VidivoxWorker {
	public static void saveMp3File(String message, File desiredName) {
		SaveMp3Worker smw = new SaveMp3Worker(message, desiredName);
		smw.execute();
	}

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
