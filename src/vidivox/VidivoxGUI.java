package vidivox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import java.awt.Color;
import javax.swing.JSeparator;

/* VideoPlayerGUI: This class deals with the GUI aspect of the video player from BigBuckPlayer 
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */
public class VidivoxGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JFrame mainFrame = this;
	private VidivoxPlayer vp;
	private VidivoxWorker vw;
	private Timer progressTimer;
	private Timer skipTimer;
	private JPanel topPanel;
	private JTextField festTextField;
	protected JLabel playerStatusLabel;
	protected JLabel chosenVideoLabel;
	protected JLabel chosenAudioLabel;
	protected JLabel progressLabel;
	private JSlider videoSlider;
	private JButton videoPlayButton;

	// -------- Constructor: creates the frame, panels, buttons, etc. ---------

	public VidivoxGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("VIDIVOX");
		setSize(950, 648);
		setLocationRelativeTo(null);
		setResizable(false);

		vp = new VidivoxPlayer();
		vw = new VidivoxWorker();
		
		// ------------------------ Top Panel ----------------------------------

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		
		// -------------------------- Player Panel -----------------------------
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout(0,0));
		playerPanel.add(vp.getPlayerComponent(), BorderLayout.CENTER);
		topPanel.add(playerPanel);
		
		
		// ------------------------ Progress Panel ------------------------------
		
		JPanel progressPanel = new JPanel();
		progressPanel.setBackground(Color.decode("#F2F1F0"));
		topPanel.add(progressPanel);
		progressPanel.setBorder(new EmptyBorder(6, 9, 2, 9));
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));

		progressLabel = new JLabel("00:00");
		progressPanel.add(progressLabel);

		progressPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		videoSlider = new JSlider();
		videoSlider.setBackground(Color.decode("#F2F1F0"));
		progressPanel.add(videoSlider);

		videoSlider.setMinimum(0);
		videoSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				vp.getPlayer().setTime(videoSlider.getValue());
			}
		});

		mainFrame.add(topPanel);

		
		// ---------------------- Controller Panel -----------------------------

		final JPanel controllerPanel = new JPanel();
		controllerPanel.setBackground(Color.decode("#F2F1F0"));
		controllerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
		topPanel.add(controllerPanel);

		final JButton videoOpenButton = new JButton("Open");
		final JPopupMenu openMenu = new JPopupMenu();

		JMenuItem openVideoMenuItem = new JMenuItem("Video file");
		JMenuItem openAudioMenuItem = new JMenuItem("Audio track");
		openMenu.add(openVideoMenuItem);
		openMenu.add(openAudioMenuItem);

		videoOpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMenu.show(videoOpenButton, videoOpenButton.getBounds().x,
						videoOpenButton.getBounds().y + videoOpenButton.getHeight());
			}
		});
		controllerPanel.add(videoOpenButton);

		openVideoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser videoChooser = new JFileChooser();
				videoChooser.setDialogTitle("Open video");
				videoChooser.setFileFilter(new VideoFilter());
				int returnValue = videoChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File desiredName = videoChooser.getSelectedFile();
					if (desiredName.exists() && desiredName != null) {
						vp.setChosenVideo(videoChooser.getSelectedFile());
						vp.playVideo();
						setPlayStatus();
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"The video file \"" + desiredName.getName() + "\" does not exist!");
					}
				}
			}
		});

		openAudioMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser audioChooser = new JFileChooser();
				audioChooser.setFileFilter(new AudioFilter());
				audioChooser.setDialogTitle("Select audio track to overlay");
				int returnValue = audioChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File desiredName = audioChooser.getSelectedFile();

					if (desiredName.exists() && desiredName != null) {
						vp.setChosenAudio(audioChooser.getSelectedFile());
						chosenAudioLabel.setText("Audio track: " + vp.getChosenAudio().getName());
					} else {
						JOptionPane.showMessageDialog(mainFrame,
								"The audio track \"" + desiredName.getName() + "\" does not exist!");
					}
				}
			}
		});

		JButton videoFBButton = new JButton("<<");
		videoFBButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRewindStatus();
				vp.skip(-1);
			}
		});
		controllerPanel.add(videoFBButton);

		videoPlayButton = new JButton("▐ ▌");
		videoPlayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (VidivoxPlayer.getVideoPaused() == 0) {
					setPauseStatus();
					vp.pause();
				} else {
					setPlayStatus();
					vp.play();
				}
			}
		});
		controllerPanel.add(videoPlayButton);

		JButton videoFFButton = new JButton(">>");
		videoFFButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFastForwardStatus();
				vp.skip(1);
			}
		});
		controllerPanel.add(videoFFButton);
		
		int defaultVolume = vp.getDefaultVolume();
		
		final JSlider volumeSlider = new JSlider(JSlider.VERTICAL, 0, 100, defaultVolume);
		volumeSlider.setToolTipText(String.valueOf(defaultVolume));
		volumeSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int volume = volumeSlider.getValue();
				vp.getPlayer().setVolume(volume);
				volumeSlider.setToolTipText(String.valueOf(volume));
			}
		});

		final JPopupMenu volumeMenu = new JPopupMenu();
		volumeMenu.add(volumeSlider);

		final JButton videoVolumeButton = new JButton("Volume");
		videoVolumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volumeMenu.show(videoVolumeButton, videoVolumeButton.getBounds().x - 200,
						videoVolumeButton.getBounds().y - 215);
			}
		});
		controllerPanel.add(videoVolumeButton);

		JButton festRemixButton = new JButton("Export");
		festRemixButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vp.getChosenVideo() == null) {
					JOptionPane.showMessageDialog(mainFrame,
							"Please select a video file to overlay the audio track into via Add > Video file");
				} else if (vp.getChosenAudio() == null) {
					JOptionPane.showMessageDialog(mainFrame,
							"Please select an audio track to overlay into the video via Add > Audio track");
				} else {
					saveChooser("video", null);
				}
			}
		});
		controllerPanel.add(festRemixButton);

		// ------------------- Festival Panel ---------------------------

		JPanel festivalPanel = new JPanel();
		festivalPanel.setBackground(Color.decode("#F2F1F0"));
		festivalPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
		topPanel.add(festivalPanel);

		JLabel festLabel = new JLabel("Festival");
		festivalPanel.add(festLabel);

		ActionListener festPlayAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldCheck()) {
					vw.festival(festTextField.getText());
				}
			}
		};

		festTextField = new JTextField();
		festTextField.setMaximumSize(festTextField.getPreferredSize());
		festivalPanel.add(festTextField);
		festTextField.setColumns(65);
		festTextField.addActionListener(festPlayAction);

		JButton festPlayButton = new JButton("Play");
		festPlayButton.addActionListener(festPlayAction);
		festivalPanel.add(festPlayButton);

		JButton festSaveButton = new JButton("Save");
		festSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldCheck()) {
					saveChooser("audio", festTextField.getText());
				}
			}
		});
		festivalPanel.add(festSaveButton);

		// ------------------- Status Panel -----------------------------

		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(Color.decode("#F2F1F0"));
		topPanel.add(statusPanel);
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 9, 6));

		playerStatusLabel = new JLabel("Status: stopped");
		statusPanel.add(playerStatusLabel);

		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		statusPanel.add(separator);

		chosenVideoLabel = new JLabel("Video: none");
		statusPanel.add(chosenVideoLabel);

		JSeparator separator_1 = new JSeparator(JSeparator.VERTICAL);
		statusPanel.add(separator_1);

		chosenAudioLabel = new JLabel("Audio track: none");
		statusPanel.add(chosenAudioLabel);

		
		// ------------------------- Player Setup ---------------------------

		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void lengthChanged(MediaPlayer player, long newTime) {
				videoSlider.setMaximum((int) newTime);
				
				String videoLength = formatLength(newTime);
				vp.setVideoLength(videoLength);
			}
		});
		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			public void opening(MediaPlayer player) {
				chosenVideoLabel.setText("Video: " + vp.getChosenVideo().getName());
			}
		});
		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			public void finished(MediaPlayer player) {
				setStoppedStatus();
				vp.stop();
			}
		});
		vp.getPlayer().setVolume(volumeSlider.getValue());

		createProgressTimer();
		createSkipTimer();

		mainFrame.setVisible(true);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ----------------------- GUI manipulation methods ---------------------
	// These methods set the GUI to a certain state matching the player state

	protected void setPlayStatus() {
		skipTimer.stop();
		videoPlayButton.setText("▐ ▌");
		playerStatusLabel.setText("Status: playing");
	}

	protected void setPauseStatus() {
		skipTimer.stop();
		videoPlayButton.setText(" ▶ ");
		playerStatusLabel.setText("Status: paused");
	}

	protected void setStoppedStatus() {
		skipTimer.stop();
		videoPlayButton.setText(" ▶ ");
		playerStatusLabel.setText("Status: stopped");
	}

	protected void setFastForwardStatus() {
		skipTimer.start();
		videoPlayButton.setText(" ▶ ");
		playerStatusLabel.setText("Status: fast-forward");
	}

	protected void setRewindStatus() {
		skipTimer.start();
		videoPlayButton.setText(" ▶ ");
		playerStatusLabel.setText("Status: rewind");
	}

	
	// ------------------- Timer creation methods --------------------------

	protected void createSkipTimer() {
		skipTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vp.getPlayer().skip(5000 * vp.getSkipDirection());
			}
		});
	}

	protected void createProgressTimer() {
		progressTimer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long currentTime = vp.getPlayer().getTime();

				progressLabel.setText(formatLength(currentTime) + " / " + vp.getVideoLength());
				videoSlider.setValue((int) vp.getPlayer().getTime());
			}
		});

		progressTimer.start();
	}

	
	// ----------------------- Miscellaneous methods ------------------------

	protected String formatLength(long videoLength) {
		/*
		 * formatLength: This method takes a long number videolength and
		 * converts it into a readable string
		 */
		int totalLength = (int) videoLength / 1000;
		int min = totalLength / 60;
		int sec = totalLength % 60;

		if (min < 60) {
			return String.format("%02d:%02d", min, sec);
		} else {
			int hour = min / 60;
			return String.format("%02d:%02d:%02d", hour, min - 60, sec);
		}
	}

	protected boolean textFieldCheck() {
		/*
		 * textFieldCheck: This method checks the text in "festTextField" for
		 * empty string and string longer than 20 words, then displays a warning
		 * message if either is found
		 */
		String message = festTextField.getText().trim();
		String[] words = message.split(" ");

		if (message.length() == 0) {
			JOptionPane.showMessageDialog(mainFrame, "The message cannot be empty!");
			return false;
		} else if (words.length > 20) {
			JOptionPane.showMessageDialog(mainFrame, "Please do not enter more than 20 words at a time!");
			return false;
		}

		return true;
	}

	protected void saveChooser(String fileType, String message) {
		/*
		 * saveChooser: This method takes a string of fileType (either "video"
		 * or "audio") and a string of festival message ("null" for video file) 
		 * and will either save the message as an mp3 file or overlay the audio
		 * track into the selected video file, through the JFileChooser.
		 */
		JFileChooser saveChooser = new JFileChooser();
		
		// Set file filter depending on the file type input
		if (fileType.equals("video")) {
			saveChooser.setDialogTitle("Choose where to save the overlayed video");
			saveChooser.setFileFilter(new VideoFilter());
		} else {
			saveChooser.setDialogTitle("Choose where to save the festival audio track");
			saveChooser.setFileFilter(new AudioFilter());
		}
		
		int returnValue = saveChooser.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File desiredName = saveChooser.getSelectedFile();
			
			desiredName = checkSuffix(desiredName, fileType);
			
			// If the file selected already exists, display a JOptionPane and ask
			// the user for overwriting confirmation
			boolean confirmSave = checkOverwrite(desiredName, fileType);
			
			if (confirmSave == true) {
				if (fileType.equals("video")) {
					String videoPath = vp.getChosenVideo().getAbsolutePath();
					String audioPath = vp.getChosenAudio().getAbsolutePath();
					
					vw.overlay(videoPath, audioPath, desiredName, vp);
				} else {
					vw.saveMp3File(message, desiredName, vp);
				}
			}
		}
	}
	
	protected File checkSuffix(File desiredName, String fileType) {
		if(fileType.equals("video")) {
			if(!desiredName.getName().endsWith(".avi")) {
				desiredName = new File(desiredName.getAbsolutePath() + ".avi");
			}
		} else {
			if(!desiredName.getName().endsWith(".mp3")) {
				desiredName = new File(desiredName.getAbsolutePath() + ".mp3");
			}
		}
		
		return desiredName;
	}
	
	protected boolean checkOverwrite(File desiredName, String fileType) {
		String chosenVideoPath = vp.getChosenVideo().getAbsolutePath();
		boolean confirmSave = false;
		
		if(desiredName.getAbsolutePath().equals(chosenVideoPath)) {
			int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
					"You are overwriting the original video file. \nAre you sure you wish to overwrite it?",
					"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			// Rename the original file to a temporary file then use it to overlay the video
			if(overwriteReponse == JOptionPane.YES_OPTION) {
				vp.setChosenVideoTemp();
				confirmSave = true;
			}
		} else if (desiredName.exists() && desiredName != null) {
			int overwriteReponse = JOptionPane.showConfirmDialog(mainFrame,
					"The " + fileType + " file \"" + desiredName.getName()
							+ "\" already exists. Do you wish to overwrite it?",
					"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			// If the user clicks yes, delete the existing file with the same name
			// and set confirmSave to true
			if (overwriteReponse == JOptionPane.YES_OPTION) {
				desiredName.delete();
				confirmSave = true;
			}
		} else {
			confirmSave = true;
		}
		
		return confirmSave;
	}
	
	// -------------- JFileChooser Filters ---------------------------
	/*
	 * These filters are nested classes that inherit FileFilter and filters
	 * the JFileChooser to display only specific file types
	 */

	class VideoFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			try {
				String[] split = Files.probeContentType(f.toPath()).split("/");
				if (split[0].equals("video") || f.isDirectory()) {
					return true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "Video";
		}

	}

	class AudioFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			try {
				String[] split = Files.probeContentType(f.toPath()).split("/");
				if (split[0].equals("audio") || f.isDirectory()) {
					return true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "Audio";
		}

	}

}
