package vidivox.gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class StatusPanel extends JPanel {
	private JLabel playerStatusLabel ;
	private JLabel chosenVideoLabel;
	private JLabel chosenAudioLabel;
	
	public StatusPanel(JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		playerStatusLabel = new JLabel("Status: stopped");
		this.add(playerStatusLabel);

		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		this.add(separator);

		chosenVideoLabel = new JLabel("Video: none");
		this.add(chosenVideoLabel);

		JSeparator separator_1 = new JSeparator(JSeparator.VERTICAL);
		this.add(separator_1);

		chosenAudioLabel = new JLabel("Audio Tracks: none");
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
