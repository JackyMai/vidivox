package vidivox.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AudioPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private AudioControlPanel audioControlPanel;
	private AudioCommentPanel audioFestivalPanel;
	private AudioScrollPanel audioScrollPanel = new AudioScrollPanel();
	
	public AudioPanel(JFrame mainFrame, StatusPanel statusPanel, PlayerControlPanel playerControlPanel) {
		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(new EmptyBorder(2, 10, 2, 10), new TitledBorder("Audio Tracks")));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.10;
		gbc.weighty = 1;
		
		
		// -------------------------- Audio Control Panel --------------------------
		
		audioControlPanel = new AudioControlPanel(mainFrame, statusPanel, audioScrollPanel, playerControlPanel);
		this.add(audioControlPanel, gbc);
		
		
		// ------------------------- Audio View Panel ---------------------------
		
		gbc.gridx = 1;
		gbc.weightx = 0.9;
		
		JPanel audioViewPanel = new JPanel();
		audioViewPanel.setLayout(new BoxLayout(audioViewPanel, BoxLayout.Y_AXIS));
		this.add(audioViewPanel, gbc);
		
		
		// --------------------------- Audio Festival Panel ----------------------------
		
		audioFestivalPanel = new AudioCommentPanel(mainFrame);
		audioViewPanel.add(audioFestivalPanel);

		
		// -------------------------- Audio View Panel -------------------------------
		
		audioViewPanel.add(audioScrollPanel);
	}

}
