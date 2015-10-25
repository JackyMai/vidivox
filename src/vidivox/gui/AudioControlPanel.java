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
import vidivox.model.AudioTrack;


public class AudioControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public AudioControlPanel(final JFrame mainFrame, final StatusPanel statusPanel, 
			final AudioScrollPanel audioScrollPanel, final PlayerControlPanel playerControlPanel) {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		
		
		// ------------------------------ Audio Add Button ------------------------------
		
		JButton audioAddButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "add.png"));
		audioAddButton.setToolTipText("Add new audio tracks");
		audioAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if(FileOpener.openAudio(mainFrame)) {
				statusPanel.setChosenAudioLabel("Audio Tracks: " + (VidivoxGUI.vm.getAudioListSize()+1));
				
				AudioTrack newTrack = new AudioTrack(VidivoxGUI.vm.getChosenAudio(), (int)VidivoxGUI.vp.getCurrentTime());
				audioScrollPanel.addAudioTrack(newTrack);
				VidivoxGUI.vm.addAudioTrack(newTrack);
				
				playerControlPanel.enableExportButton();
			}
		}});
		removeButtonStyle(audioAddButton);
		this.add(audioAddButton, gbc);
		
		
		// ------------------------------ Audio Edit Button ------------------------------
		
		JButton audioEditButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "edit.png"));
		audioEditButton.setToolTipText("Edit insert time of selected tracks");
		audioEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioEditDialog dialog = new AudioEditDialog(mainFrame, audioScrollPanel);
				dialog.setVisible(true);
			}
		});
		removeButtonStyle(audioEditButton);
		this.add(audioEditButton, gbc);
		
		
		// ------------------------------ Audio Remove Button ------------------------------
		
		JButton audioRemoveButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "delete.png"));
		audioRemoveButton.setToolTipText("Remove selected audio tracks");
		audioRemoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = audioScrollPanel.getSelectedRows();
				for(int i=selectedRows.length-1; i>=0; i--) {
					audioScrollPanel.removeSelectedRow(selectedRows[i]);
					VidivoxGUI.vm.removeAudioTrack(selectedRows[i]);
					statusPanel.setChosenAudioLabel("Audio Tracks: " + String.valueOf(VidivoxGUI.vm.getAudioListSize()));
				}
			}
		});
		removeButtonStyle(audioRemoveButton);
		this.add(audioRemoveButton, gbc);
	}
	
	protected void removeButtonStyle(JButton button) {
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
	}
}
