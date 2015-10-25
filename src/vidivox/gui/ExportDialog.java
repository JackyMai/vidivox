package vidivox.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import vidivox.worker.OverlayWorker;

public class ExportDialog extends JDialog implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private JOptionPane exportOptionPane;
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private String cancelButton = "Cancel";
	private OverlayWorker ow;
	
	public ExportDialog(JFrame mainFrame, String videoPath, File desiredName) {
		String[] audioPath = VidivoxGUI.vm.getDelayedAudioList();
		ow = new OverlayWorker(this, videoPath, audioPath, desiredName);
		ow.execute();
		
		String message = "Exporting the video as requested";
		
		progressBar.setIndeterminate(true);
		
		Object[] structure = {message, progressBar};
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
			
			if(cancelButton.equals(value)) {
				ow.cancel(true);
				dispose();
			}
		}
	}
}
