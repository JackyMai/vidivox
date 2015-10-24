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
		
		JButton audioAddButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "add.png"));
		removeButtonStyle(audioAddButton);
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
		this.add(audioAddButton, gbc);
		
		JButton audioEditButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "edit.png"));
		removeButtonStyle(audioEditButton);
		audioEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		this.add(audioEditButton, gbc);
		
		JButton audioDeleteButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "delete.png"));
		removeButtonStyle(audioDeleteButton);
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
	}
	
	protected void removeButtonStyle(JButton button) {
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
	}
	
	protected void audioEditWindow() {
		
	}
}
