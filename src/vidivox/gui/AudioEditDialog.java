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

public class AudioEditDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private JOptionPane editOptionPane;
	private AudioScrollPanel audioScrollPanel;
	private int[] selectedRows;
	private JTextField editTextField;
	private String newTime;
	private String cancelButton = "Cancel";
	private String okButton = "OK";
	
	public AudioEditDialog(JFrame mainFrame, AudioScrollPanel asp) {
		this.audioScrollPanel = asp;
		this.selectedRows = audioScrollPanel.getSelectedRows();
		
		this.setLocationRelativeTo(mainFrame);
		this.setModal(true);
		
		String message = "Please enter a new insert time for the audio tracks";
		String instruction = "Enter in the (HH:)MM:SS format: ";
		
		editTextField = new JTextField();
		
		Object[] structure = {message, instruction, editTextField};
		Object[] options = {cancelButton, okButton};
		
		editOptionPane = new JOptionPane(structure,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION,
				null,
				options,
				options[1]);
		
		this.setContentPane(editOptionPane);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				editTextField.requestFocusInWindow();
			}
		});
		
		editTextField.addActionListener(this);
		editOptionPane.addPropertyChangeListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		editOptionPane.setValue(okButton);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();
		
		if(isVisible()
			&& (e.getSource() == editOptionPane) 
			&& (JOptionPane.VALUE_PROPERTY.equals(property))
			|| JOptionPane.INPUT_VALUE_PROPERTY.equals(property)) {
			
			Object value = editOptionPane.getValue();
			if(value == JOptionPane.UNINITIALIZED_VALUE) {
				return;
			}
			
			editOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			if(okButton.equals(value)) {
				newTime = editTextField.getText();
				
				if(GenericHelper.checkValidInsertTime(newTime)) {
					for(int selected : selectedRows) {
						audioScrollPanel.setModelValue(newTime, selected, 1);
					}
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, 
							"Sorry but \"" + newTime + "\" is not a valid insert time!\n"
							+ "Please enter according to the (HH:)MM:SS format and make"
							+ "the new time is between 00:00 and video length.",
							"OK",
							JOptionPane.ERROR_MESSAGE);
					newTime = null;
					editTextField.requestFocusInWindow();
				}
			} else {
				newTime = null;
				dispose();
			}
		}
	}

}
