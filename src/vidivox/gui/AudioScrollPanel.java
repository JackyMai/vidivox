package vidivox.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import vidivox.helper.TimeFormatter;
import vidivox.model.AudioTrack;

/**
 * This class contains a JScrollPane which has the JTable and table model
 * for the audio tracks added by the user.
 * 
 * Authors: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private AudioControlPanel audioControlPanel;
	private JTable audioTrackTable;
	private DefaultTableModel audioTrackModel;
	
	@SuppressWarnings("serial")
	public AudioScrollPanel() {
		// Instantiates the table model and overrides the method to make it not editable.
		audioTrackModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Sets the column header of the table model
		audioTrackModel.setColumnIdentifiers(new String[]{"Audio Track", "Insert At"});
		audioTrackTable = new JTable(audioTrackModel);
		
		// Only enable the audio edit button if one or more elements in the table is being selected
		// The use of ListSelectionListener is inspired from the following reference:
		// http://stackoverflow.com/a/4400050
		audioTrackTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					audioControlPanel.enableEditButton(audioTrackTable.getSelectedRowCount() > 0);
				}
			}
		});
		
		// Although setPrefferedSize is often not encouraged in Java, this is necessary
		// to prevent the table from collapsing/expanding after start-up.
		this.setViewportView(audioTrackTable);
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 86));
	}
	
	/**
	 * Returns the table model of the audio track added by the users
	 * @return
	 */
	protected DefaultTableModel getAudioTrackModel() {
		return audioTrackModel;
	}
	
	/**
	 * Adds the provided AudioTrack object into the table model of the audio tracks.
	 * @param newTrack - an object of type AudioTrack which will be added to the table model.
	 */
	protected void addAudioTrack(AudioTrack newTrack) {
		audioTrackModel.addRow(new Object[]{newTrack.getAudioName(), TimeFormatter.milliToString(newTrack.getInsertTime())});
	}
	
	/**
	 * Returns an int[] with the index number of the selected rows of the JTable
	 * for the audio tracks.
	 * @return integer array with the index numbers of the selected rows
	 */
	protected int[] getSelectedRows() {
		return audioTrackTable.getSelectedRows();
	}
	
	/**
	 * Sets the object value of the table model for the cell at the 
	 * specified row and and column.
	 * @param newValue - a string with the new value
	 * @param row - the row whose value is to be changed
	 * @param column - the column whose value is to be changed
	 */
	protected void setModelValue(String newValue, int row, int column) {
		this.audioTrackModel.setValueAt(newValue, row, column);
		VidivoxGUI.vm.setInsertTime(row, TimeFormatter.stringToMilli(newValue));
	}
	
	/**
	 * Removes the specified row from the table model.
	 * @param row - the row index of the row to be removed
	 */
	protected void removeSelectedRow(int row) {
		audioTrackModel.removeRow(row);
	}
	
	protected void getAudioControlPanel(final AudioControlPanel audioControlPanel) {
		this.audioControlPanel = audioControlPanel;
	}
}
