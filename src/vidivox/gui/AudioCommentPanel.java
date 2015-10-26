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
import vidivox.worker.FestivalWorker;
import vidivox.worker.VidivoxWorker;

/**
 * This class contains a JPanel that has a JTextField and JButtons for
 * the Festival functionalities. More specifically it allows the user
 * to enter text in the textfield and preview it or save it as an mp3 file.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class AudioCommentPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField festTextField;
	private JButton playButton;
	private JButton saveButton;
	private FestivalWorker fw;
	
	public AudioCommentPanel(final JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setBorder(new EmptyBorder(0, 0, 3, 2));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel commentLabel = new JLabel("Comment");
		this.add(commentLabel);
		
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		
		// -------------------------- Festival TextField --------------------------
		// This textfield will allow the user to enter up to 140 characters and then
		// either play the message entered or save it as an mp3 file through the JButtons.
		festTextField = new JTextField();
		festTextField.setDocument(new TextLimit(140));
		festTextField.setText("There is a 140 character limit");
		festTextField.addActionListener(this);
		festTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, festTextField.getPreferredSize().height));
		
		// Disables the saveButton if the textfield is empty after releasing a key
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
		
		
		// -------------------------- Play Button --------------------------
		// This button will play the text message entered in the festTextField
		// through the voice of Festival. The user may press this button again
		// which Festival is still speaking to stop it.
		playButton = new JButton("Play");
		playButton.setMargin(new Insets(2, 6, 2, 6));
		playButton.setToolTipText("Preview entered message");
		playButton.addActionListener(this);
		this.add(playButton);
		
		this.add(Box.createRigidArea(new Dimension(3, 0)));
		
		
		// -------------------------- Save Button --------------------------
		// This button will save the text message entered in the festTextField
		// as an mp3 file by showing the user a JFileChooser
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Decides the action of the play button depending on the text in it.
		// Play button will create a new FestivalWorker if the button says "Play"
		// and otherwise kills the existing FestivalWorker if it says "Stop"
		if(playButton.getText().equals("Play")) {
			fw = VidivoxWorker.festival(festTextField.getText(), playButton);
			playButton.setText("Stop");
		} else {
			fw.cancel(true);
		}
	}
}
