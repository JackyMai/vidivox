package vidivox.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import vidivox.helper.GenericHelper;

/**
 * This class creates a customised JOptionPane which checks whether
 * the new insert time entered by the user is valid or not.
 * Reference: http://stackoverflow.com/a/13055405
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioEditDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private JOptionPane editOptionPane;
	private AudioScrollPanel audioScrollPanel;
	private int[] selectedRows;
	private JTextField editTextField = new JTextField();
	private String newTime;
	private String okButton = "OK";
	private String cancelButton = "Cancel";
	
	public AudioEditDialog(JFrame mainFrame, AudioScrollPanel asp) {
		// Storing the input arguments as global variables
		this.audioScrollPanel = asp;
		this.selectedRows = audioScrollPanel.getSelectedRows();

		// Setting up the instructions to help the user to enter the correct insert time
		String message = "Please enter a new insert time for the audio tracks";
		String instruction = "Enter in the (HH:)MM:SS format (see time label): ";
		
		Object[] structure = {message, instruction, editTextField};
		Object[] options = {okButton, cancelButton};
		
		// Setting up the JOptionPane which will display the labels, textfield and buttons
		// in the correct order
		editOptionPane = new JOptionPane(structure,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				options,
				options[0]);
		
		// Some minor settings for the JOptionPane
		this.setContentPane(editOptionPane);
		this.setTitle("Edit Insert Time");
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				editTextField.requestFocusInWindow();
			}
		});
		
		// Adding the necessary listeners for the textfield and JOptionPane
		editTextField.addActionListener(this);
		editOptionPane.addPropertyChangeListener(this);
		
		// Packing and setting the relative location of the window to make
		// sure the JOptionPane will display properly
		pack();
		this.setLocationRelativeTo(mainFrame);
	}
	
	/**
	 * This method handles events for the editTextField.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		editOptionPane.setValue(okButton);
	}
	
	/**
	 * This methods will respond to the state changes in the JOptionPane.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();
		
		if(isVisible()
			&& (e.getSource() == editOptionPane) 
			&& (JOptionPane.VALUE_PROPERTY.equals(property))
			|| JOptionPane.INPUT_VALUE_PROPERTY.equals(property)) {
			
			Object value = editOptionPane.getValue();
			if(value == JOptionPane.UNINITIALIZED_VALUE) {
				// Ignore because value is still uninitialised.
				return;
			}
			
			// Resetting the value of JOptionPane.
			// Otherwise nothing will happen if the user presses the same button more than once.
			editOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			if(okButton.equals(value)) {
				newTime = editTextField.getText();
				
				// If the new insert time entered by the user is valid,
				// then loop through all of the selected rows in the table and change
				// the insert time in the AudioTrack object to the new time.
				if(GenericHelper.checkValidInsertTime(newTime)) {
					for(int selected : selectedRows) {
						audioScrollPanel.setModelValue(newTime, selected, 1);
					}
					
					dispose();
				} else {
					// Display an error message telling the user that the new insert time is invalid.
					JOptionPane.showMessageDialog(this, 
							"Sorry but \"" + newTime + "\" is not a valid insert time!\n"
							+ "Please enter a valid time between 00:00 up to " + VidivoxGUI.vp.getVideoLength() +".\n"
							+ "The (HH:)MM:SS format is hinted next to the video slider.",
							"OK",
							JOptionPane.ERROR_MESSAGE);
					
					newTime = null;
					editTextField.requestFocusInWindow();
				}
			} else {
				// The user either closed the dialog or pressed the cancel button.
				newTime = null;
				dispose();
			}
		}
	}

}
