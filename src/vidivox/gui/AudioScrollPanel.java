package vidivox.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import vidivox.helper.TimeFormatter;
import vidivox.model.AudioTrack;

public class AudioScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private JTable audioTrackTable;
	private DefaultTableModel audioTrackModel;
	
	@SuppressWarnings("serial")
	public AudioScrollPanel() {
		audioTrackModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		audioTrackModel.setColumnIdentifiers(new String[]{"Audio Track", "Insert At"});
		audioTrackTable = new JTable(audioTrackModel);
		
		this.setViewportView(audioTrackTable);
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 86));
	}
	
	protected DefaultTableModel getAudioTrackModel() {
		return audioTrackModel;
	}
	
	protected void addAudioTrack(AudioTrack newTrack) {
		audioTrackModel.addRow(new Object[]{newTrack.getAudioName(), TimeFormatter.formatLength(newTrack.getInsertTime())});
	}
	
	protected int[] getSelectedRows() {
		return audioTrackTable.getSelectedRows();
	}
	
	protected void removeSelectedRow(int row) {
		audioTrackModel.removeRow(row);
	}
}
