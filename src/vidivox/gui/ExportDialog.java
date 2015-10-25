package vidivox.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import vidivox.worker.OverlayWorker;

/**
 * This class will create a customised JOptionPane that informs the user of the
 * export operation with a message and a progress bar and allows the user the
 * to cancel the operation.
 * Reference: http://stackoverflow.com/a/13055405
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class ExportDialog extends JDialog implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private JOptionPane exportOptionPane;
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private String cancelButton = "Cancel";
	private OverlayWorker ow;
	
	public ExportDialog(JFrame mainFrame, String videoPath, File desiredName) {
		// Instantiates an OverlayWorker to start the export operation.
		String[] audioPath = VidivoxGUI.vm.getDelayedAudioList();
		ow = new OverlayWorker(this, videoPath, audioPath, desiredName);
		ow.execute();
		
		// Setting up the instructions to help the user to enter the correct insert time
		String message = "Exporting " + desiredName.getName() + " as requsted.";
		String message2 = "Please give it moment, this shouldn't take long.";
		
		progressBar.setIndeterminate(true);
		
		Object[] structure = {message, message2, progressBar};
		Object[] options = {cancelButton};
		
		// Setting up the JOptionPane which will display the labels, 
		// progress bar and buttons in the correct order
		exportOptionPane = new JOptionPane(structure,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.CANCEL_OPTION,
				null,
				options,
				options[0]);
		
		// Some minor settings for the JOptionPane
		this.setContentPane(exportOptionPane);
		this.setTitle("Export Operation");
		this.setModal(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Adding the necessary listeners for the JOptionPane
		exportOptionPane.addPropertyChangeListener(this);
		
		// Packing and setting the relative location of the window to make
		// sure the JOptionPane will display properly
		pack();
		this.setLocationRelativeTo(mainFrame);
	}

	/**
	 * This methods will respond to the state changes in the JOptionPane.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();
		
		if(isVisible()
			&& (e.getSource() == exportOptionPane) 
			&& (JOptionPane.VALUE_PROPERTY.equals(property))
			|| JOptionPane.INPUT_VALUE_PROPERTY.equals(property)) {
			
			Object value = exportOptionPane.getValue();
			if(value == JOptionPane.UNINITIALIZED_VALUE) {
				// Ignore because value is still uninitialised.
				return;
			}
			
			// If the cancel button is pressed, cancel the overlay worker
			// and dispose the window.
			if(cancelButton.equals(value)) {
				ow.cancel(true);
				dispose();
			}
		}
	}
}
