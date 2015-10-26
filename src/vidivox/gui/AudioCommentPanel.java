package vidivox.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

/**
 * This class contains a JPanel 
 * @author jacky
 *
 */
public class AudioCommentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField festTextField;
	private JButton saveButton;
	
	public AudioCommentPanel(final JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setBorder(new EmptyBorder(0, 0, 3, 2));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel commentLabel = new JLabel("Comment");
		this.add(commentLabel);

		ActionListener commentPlayAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VidivoxWorker.festival(festTextField.getText());
			}
		};
		
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		
		festTextField = new JTextField();
		festTextField.setDocument(new TextLimit(140));
		festTextField.setText("There is a 140 character limit");
		festTextField.addActionListener(commentPlayAction);
		festTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, festTextField.getPreferredSize().height));
		festTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(festTextField.getText().length() == 0) {
					saveButton.setEnabled(false);
				} else {
					saveButton.setEnabled(true);
				}
			}
		});
		this.add(festTextField);
		
		this.add(Box.createRigidArea(new Dimension(3, 0)));
		
		JButton playButton = new JButton("Play");
		playButton.setMargin(new Insets(2, 6, 2, 6));
		playButton.setToolTipText("Preview entered message");
		playButton.addActionListener(commentPlayAction);
		this.add(playButton);
		
		this.add(Box.createRigidArea(new Dimension(3, 0)));
		
		saveButton = new JButton("Save");
		saveButton.setMargin(new Insets(2, 6, 2, 6));
		saveButton.setToolTipText("Save message as audio file");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "audio", festTextField.getText());
			}
		});
		this.add(saveButton);
	}
}
