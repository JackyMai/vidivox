package vidivox.gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * This class includes a JPanel 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class StatusPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel playerStatusLabel ;
	private JLabel chosenVideoLabel;
	private JLabel chosenAudioLabel;
	
	public StatusPanel(JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));

		playerStatusLabel = new JLabel("Status: stopped");
		this.add(playerStatusLabel);

		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		this.add(separator);

		chosenVideoLabel = new JLabel("Video: none");
		this.add(chosenVideoLabel);

		JSeparator separator_1 = new JSeparator(JSeparator.VERTICAL);
		this.add(separator_1);

		chosenAudioLabel = new JLabel("Audio Tracks: 0");
		this.add(chosenAudioLabel);
	}

	protected void setPlayerStatusLabel(String playerStatus) {
		this.playerStatusLabel.setText(playerStatus);
	}

	protected void setChosenVideoLabel(String chosenVideo) {
		this.chosenVideoLabel.setText(chosenVideo);
	}

	protected void setChosenAudioLabel(String chosenAudio) {
		this.chosenAudioLabel.setText(chosenAudio);
	}
	
	
}
