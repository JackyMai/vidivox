package vidivox.gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * This class includes a JPanel that holds the JLabels for informing
 * the user of Vidivox's status. This includes the status of the video
 * player (e.g. playing, paused), the name of the chosen video, and
 * the number of audio tracks currently's added. 
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class StatusPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel playerStatusLabel ;
	private JLabel chosenVideoLabel;
	private JLabel chosenAudioLabel;
	
	public StatusPanel(JFrame mainFrame) {
		this.setBackground(Color.decode("#F3F3F3"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));

		// -------------------------- Player Status Label --------------------------
		
		playerStatusLabel = new JLabel("Status: stopped");
		this.add(playerStatusLabel);

		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		this.add(separator);

		
		// -------------------------- Chosen Video Label --------------------------
		
		chosenVideoLabel = new JLabel("Video: none");
		this.add(chosenVideoLabel);

		JSeparator separator_1 = new JSeparator(JSeparator.VERTICAL);
		this.add(separator_1);

		
		// -------------------------- Chosen Audio Label --------------------------
		
		chosenAudioLabel = new JLabel("Audio Tracks: 0");
		this.add(chosenAudioLabel);
	}
	
	/**
	 * Sets the text displayed on the playerStatusLabel to the string provided
	 * @param playerStatus - a string containing the new status of the player
	 */
	protected void setPlayerStatusLabel(String playerStatus) {
		this.playerStatusLabel.setText(playerStatus);
	}
	
	/**
	 * Sets the text displayed on the chosenVideoLabel to the string provided
	 * @param chosenVideo - a string containing the name of the new video
	 */
	protected void setChosenVideoLabel(String chosenVideo) {
		this.chosenVideoLabel.setText(chosenVideo);
	}

	/**
	 * Sets the text displayed on the chosenAudioLabel to the string provided
	 * @param chosenAudio - a string containing the total number of audio tracks added
	 */
	protected void setChosenAudioLabel(String chosenAudio) {
		this.chosenAudioLabel.setText(chosenAudio);
	}
	
	
}
