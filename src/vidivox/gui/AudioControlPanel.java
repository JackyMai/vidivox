package vidivox.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import vidivox.filechooser.FileOpener;
import vidivox.filechooser.FileSaver;
import vidivox.model.AudioTrack;

public class AudioControlPanel extends JPanel {
	private JButton exportButton;
	
	public AudioControlPanel(final JFrame mainFrame, final StatusPanel statusPanel, final AudioScrollPanel audioScrollPanel) {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		
		JButton audioAddButton = new JButton("Add");
		audioAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if(FileOpener.openAudio(mainFrame)) {
				statusPanel.setChosenAudioLabel("Audio track: " + VidivoxGUI.vm.getChosenAudio().getName());
				
				AudioTrack newTrack = new AudioTrack(VidivoxGUI.vm.getChosenAudio(), (int)VidivoxGUI.vp.getCurrentTime());
				audioScrollPanel.addAudioTrack(newTrack);
				VidivoxGUI.vm.addAudioTrack(newTrack);
				
				enableExportButton();
			}
		}});
		this.add(audioAddButton, gbc);
		
		JButton audioEditButton = new JButton("Edit");
		this.add(audioEditButton, gbc);
		
		JButton audioDeleteButton = new JButton("Delete");
		audioDeleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int selectedRow : audioScrollPanel.getSelectedRows()) {
					audioScrollPanel.removeSelectedRow(selectedRow);
					VidivoxGUI.vm.removeAudioTrack(selectedRow);
				}
			}
		});
		this.add(audioDeleteButton, gbc);
		
		exportButton = new JButton("Export");
//		exportButton.setMargin(new Insets(2,6,2,6));
		exportButton.setEnabled(false);
		exportButton.addActionListener(new ActionListener() {
			@Override
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
