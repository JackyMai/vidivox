package vidivox.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import vidivox.filechooser.FileSaver;
import vidivox.helper.TextLimit;
import vidivox.worker.VidivoxWorker;

public class AudioFestivalPanel extends JPanel {
	private JTextField festivalTextField;
	
	public AudioFestivalPanel(final JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setBorder(new EmptyBorder(0, 10, 0, 10));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel commentLabel = new JLabel("Comment");
		this.add(commentLabel);

		ActionListener commentPlayAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VidivoxWorker.festival(festivalTextField.getText());
			}
		};
		
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		
		festivalTextField = new JTextField();
		festivalTextField.setDocument(new TextLimit(140));
		festivalTextField.addActionListener(commentPlayAction);
		festivalTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, festivalTextField.getPreferredSize().height));
		this.add(festivalTextField);
		
		this.add(Box.createRigidArea(new Dimension(3, 0)));
		
		JButton playButton = new JButton("Play");
		playButton.setMargin(new Insets(2, 6, 2, 6));
		playButton.addActionListener(commentPlayAction);
		this.add(playButton);
		
		this.add(Box.createRigidArea(new Dimension(3, 0)));
		
		JButton saveButton = new JButton("Save");
		saveButton.setMargin(new Insets(2, 6, 2, 6));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "audio", festivalTextField.getText());
			}
		});
		this.add(saveButton);
	}
}
