package vidivox.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * This class is the top-level audio JPanel that holds all of the other audio panels.
 * This class only contains the main structure and layout for the audio panels rather
 * than showing all the buttons and details.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private AudioControlPanel audioControlPanel;
	private AudioCommentPanel audioFestivalPanel;
	private AudioScrollPanel audioScrollPanel = new AudioScrollPanel();
	
	public AudioPanel(JFrame mainFrame, StatusPanel statusPanel, PlayerControlPanel playerControlPanel) {
		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(new EmptyBorder(2, 10, 2, 10), new TitledBorder("Audio Tracks")));
		
		// Setting up the GridBagConstraints to format the JPanel
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		
		// -------------------------- Audio Control Panel --------------------------
		// The AudioControlPanel will only take up 10% of the total AudioPanel space 
		// in the x-axis.
		gbc.weightx = 0.1;
		
		audioControlPanel = new AudioControlPanel(mainFrame, statusPanel, audioScrollPanel, playerControlPanel);
		this.add(audioControlPanel, gbc);
		
		
		// ------------------------- Audio View Panel ---------------------------
		// The AudioViewPanel will take up the rest of the 90% of the AudioPanel space
		// in the x-axis.
		gbc.gridx = 1;
		gbc.weightx = 0.9;
		
		JPanel audioViewPanel = new JPanel();
		audioViewPanel.setLayout(new BoxLayout(audioViewPanel, BoxLayout.Y_AXIS));
		this.add(audioViewPanel, gbc);
		
		
		// --------------------------- Audio Festival Panel ----------------------------
		
		audioFestivalPanel = new AudioCommentPanel(mainFrame);
		audioViewPanel.add(audioFestivalPanel);

		
		// -------------------------- Audio Scroll Panel -------------------------------
		
		audioScrollPanel.getAudioControlPanel(audioControlPanel);
		audioViewPanel.add(audioScrollPanel);
	}

}
