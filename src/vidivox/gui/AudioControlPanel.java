package vidivox.gui;

import java.awt.Color;
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

/**
 * This class is a JPanel that provides control for the audio tracks added
 * by the user. This includes actions such as add, edit and remove audio tracks
 * from the JTable in the AudioScrollPanel.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton audioEditButton;
	private JButton audioRemoveButton;

	public AudioControlPanel(final JFrame mainFrame, final StatusPanel statusPanel, 
			final AudioScrollPanel audioScrollPanel, final PlayerControlPanel playerControlPanel) {
		this.setBackground(Color.decode("#F3F3F3"));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		
		// -------------------------- Audio Add Button --------------------------
		// This button will bring up a JFileChooser which allows the user to add
		// audio tracks in the mp3 format to the JTable. Which then can be overlaid
		// on top of a video file to produce a merged video.
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
				enableRemoveButton();
				playerControlPanel.enableExportButton();
			}
		}});
		removeButtonStyle(audioAddButton);
		this.add(audioAddButton, gbc);
		
		
		// -------------------------- Audio Edit Button --------------------------
		// This button will allow the user to edit the insert time of the selected
		// audio tracks from the JTable.
		audioEditButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "edit.png"));
		audioEditButton.setToolTipText("Edit insert time of selected tracks");
		audioEditButton.setEnabled(false);
		audioEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioEditDialog dialog = new AudioEditDialog(mainFrame, audioScrollPanel);
				dialog.setVisible(true);
			}
		});
		removeButtonStyle(audioEditButton);
		this.add(audioEditButton, gbc);
		
		
		// -------------------------- Audio Remove Button --------------------------
		// This button will allow the user to remove the selected audio tracks from the JTable
		// This means that the audio files removed will no longer be merged with the 
		// video file during export.
		audioRemoveButton = new JButton(new ImageIcon("src" + File.separator + "icons" + File.separator + "delete.png"));
		audioRemoveButton.setToolTipText("Remove selected audio tracks");
		
		// This listening will obtain the index of the selected rows and remove
		// the it from the audio track table model and ArrayList. It will also
		// check whether the remove and export button should be enabled after
		// such action.
		audioRemoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = audioScrollPanel.getSelectedRows();
				for(int i=selectedRows.length-1; i>=0; i--) {
					audioScrollPanel.removeSelectedRow(selectedRows[i]);
					VidivoxGUI.vm.removeAudioTrack(selectedRows[i]);
					
					statusPanel.setChosenAudioLabel("Audio Tracks: " + String.valueOf(VidivoxGUI.vm.getAudioListSize()));
					
					enableRemoveButton();
					playerControlPanel.enableExportButton();
				}
			}
		});
		removeButtonStyle(audioRemoveButton);
		this.add(audioRemoveButton, gbc);
		
		enableRemoveButton();
	}
	
	/**
	 * Enables or disables the edit button depending on the boolean
	 * input provided.
	 * @param set - the boolean value to enable or disable the edit button
	 */
	protected void enableEditButton(boolean set) {
		audioEditButton.setEnabled(set);
	}
	
	/**
	 * Enables the remove button only if the audio track list is not empty
	 */
	protected void enableRemoveButton() {
		if(VidivoxGUI.vm.getAudioListSize() != 0) {
			audioRemoveButton.setEnabled(true);
		} else {
			audioRemoveButton.setEnabled(false);
		}
	}
	
	/**
	 * Takes a JButton and removes all margin, border or filled colour
	 * from the default setting.
	 * @param button - 
	 */
	protected void removeButtonStyle(JButton button) {
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
	}
}
