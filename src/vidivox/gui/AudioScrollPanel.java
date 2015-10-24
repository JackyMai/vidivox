package vidivox.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AudioScrollPanel extends JScrollPane {
	private JTable audioTrackTable;
	private DefaultTableModel audioTrackModel;
	
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
//		JScrollPane audioScrollPanel = new JScrollPane(audioTrackTable);
//		this.setPreferredSize(new Dimension(audioFestivalPanel.getPreferredSize().width, 86));
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 86));
	}
}
