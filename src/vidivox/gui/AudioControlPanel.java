package vidivox.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import vidivox.filechooser.FileOpener;
import vidivox.filechooser.FileSaver;
import vidivox.helper.TimeFormatter;
import vidivox.model.AudioTrack;

public class AudioControlPanel extends JPanel {
	private JButton exportButton;
	
	public AudioControlPanel(final JFrame mainFrame) {
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		
		JButton audioAddButton = new JButton("Add");
		audioAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if(FileOpener.openAudio(mainFrame)) {
//				chosenAudioLabel.setText("Audio track: " + vp.getChosenAudio().getName());
				
				AudioTrack newTrack = new AudioTrack(VidivoxGUI.vm.getChosenAudio(), (int)VidivoxGUI.vp.getCurrentTime());
				commentModel.addRow(new Object[]{newTrack.getAudioName(), TimeFormatter.formatLength(newTrack.getInsertTime())});
				VidivoxGUI.vm.addAudioTrack(newTrack);
				
				enableExportButton();
			}
		}
		});
		this.add(audioAddButton, gbc);
		
		JButton audioEditButton = new JButton("Edit");
		this.add(audioEditButton, gbc);
		
		JButton audioDeleteButton = new JButton("Delete");
		this.add(audioDeleteButton, gbc);
		
		exportButton = new JButton("Export");
		exportButton.setMargin(new Insets(2,6,2,6));
		exportButton.setEnabled(false);
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "video", null);
			}
		});
		this.add(exportButton, gbc);
	}
	
	protected void enableExportButton() {
		if(VidivoxGUI.vm.getChosenVideo() != null && VidivoxGUI.vm.getChosenAudio() != null) {
			exportButton.setEnabled(true);
		}
	}
}
