package vidivox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import vidivox.filechooser.FileOpener;
import vidivox.filechooser.FileSaver;
import vidivox.helper.TextLimit;
import vidivox.helper.TimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JTable;

import java.awt.Color;
import javax.swing.JSeparator;

/* VideoPlayerGUI: This class deals with the GUI aspect of the video player from BigBuckPlayer 
 * 
 * Authors Jacky Mai
 * UPI: jmai871
 */
public class VidivoxGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JFrame mainFrame = this;
	public static VidivoxPlayer vp;
	public static VidivoxModel vm;
	private Timer progressTimer;
	private Timer skipTimer;
	private JPanel topPanel;
	private JTextField commentTextField;
	protected JLabel playerStatusLabel;
	protected JLabel chosenVideoLabel;
	protected JLabel chosenAudioLabel;
	protected JLabel progressLabel;
	private JSlider videoSlider;
	private JButton videoRWButton;
	private JButton videoPlayButton;
	private JButton videoFFButton;
	private JButton videoExportButton;
	private JTable commentTable;
	private DefaultTableModel commentModel;
	
	// -------- Constructor: creates the frame, panels, buttons, etc. ---------

	@SuppressWarnings("serial")
	public VidivoxGUI() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("VIDIVOX");
		setSize(950, 776);
		setLocationRelativeTo(null);
		setResizable(false);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String buttons[] = {"Yes", "No"};
				int returnValue = JOptionPane.showOptionDialog(mainFrame, "Are you sure that you want to exist Vidivox?\n"
						+ "All comments will need to be re-added.",
						"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, buttons, buttons[1]);
				 
				if(returnValue == 0) {
					System.exit(0);
				}
			}
		});
		
		vp = new VidivoxPlayer();
		vm = new VidivoxModel();
		
		createDir();
		
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
		videoSlider.setUI(new MetalSliderUI() {
			@Override
			protected void scrollDueToClickInTrack(int direction) {
		        int value = this.valueForXPosition(videoSlider.getMousePosition().x);
		        vp.getPlayer().setTime(value);
		    }
		});
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
				if(FileOpener.openVideo(mainFrame)) {
					setPlayStatus();
					enableExportButton();
				}
			}
		});

		openAudioMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(FileOpener.openAudio(mainFrame)) {
//					chosenAudioLabel.setText("Audio track: " + vp.getChosenAudio().getName());
					AudioTrack newTrack = new AudioTrack(vm.getChosenAudio(), (int)vp.getCurrentTime());
					commentModel.addRow(new Object[]{newTrack.getAudioName(), TimeFormatter.formatLength(newTrack.getInsertTime())});
					vm.addAudioTrack(newTrack);
					
					enableExportButton();
				}
			}
		});

		videoRWButton = new JButton("<<");
		videoRWButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRewindStatus();
				vp.skip(-1);
			}
		});
		controllerPanel.add(videoRWButton);

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

		videoFFButton = new JButton(">>");
		videoFFButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFastForwardStatus();
				vp.skip(1);
			}
		});
		controllerPanel.add(videoFFButton);
		
		enablePlayerControl(false);
		
		int defaultVolume = vp.getDefaultVolume();
		
		final JSlider volumeSlider = new JSlider(JSlider.VERTICAL, 0, 150, defaultVolume);
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

		videoExportButton = new JButton("Export");
		videoExportButton.setEnabled(false);
		videoExportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "video", null);
			}
		});
		controllerPanel.add(videoExportButton);

		// ------------------- Comment Panel ---------------------------
		
		JPanel commentPanel = new JPanel();
		commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
		topPanel.add(commentPanel);
		
		
		// --------------------------- Comment Input Panel ----------------------------
		
		JPanel commentInputPanel = new JPanel();
		commentInputPanel.setBackground(Color.decode("#F2F1F0"));
		commentInputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
		commentPanel.add(commentInputPanel);
		
		JLabel commentLabel = new JLabel("Comment");
		commentInputPanel.add(commentLabel);

		ActionListener commentPlayAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VidivoxWorker.festival(commentTextField.getText());
			}
		};

		commentTextField = new JTextField();
		commentTextField.setDocument(new TextLimit(140));
		commentTextField.setMaximumSize(commentTextField.getPreferredSize());
		commentInputPanel.add(commentTextField);
		commentTextField.setColumns(65);
		commentTextField.addActionListener(commentPlayAction);

		JButton commentPlayButton = new JButton("Play");
		commentPlayButton.addActionListener(commentPlayAction);
		commentInputPanel.add(commentPlayButton);

		JButton commentSaveButton = new JButton("Save");
		commentSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "audio", commentTextField.getText());
			}
		});
		commentInputPanel.add(commentSaveButton);

		
		// -------------------------- Comment Table Panel -------------------------------
		
		JPanel commentTablePanel = new JPanel();
		commentTablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 6));
		commentPanel.add(commentTablePanel);
		
		commentModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		commentModel.setColumnIdentifiers(new String[]{"Audio Track", "Insert At"});
		commentTable = new JTable(commentModel);
		
		JScrollPane commentScrollPane = new JScrollPane(commentTable);
		commentScrollPane.setViewportView(commentTable);
		commentScrollPane.setPreferredSize(new Dimension(commentInputPanel.getPreferredSize().width-4, 86));
		commentTablePanel.add(commentScrollPane);
		
		
		// ------------------- Status Panel -----------------------------
		JSeparator statusSeparator = new JSeparator();
		topPanel.add(statusSeparator);
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(Color.decode("#F2F1F0"));
		topPanel.add(statusPanel);
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

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
				
				String videoLength = TimeFormatter.formatLength(newTime);
				vp.setVideoLength(videoLength);
			}
			
			public void opening(MediaPlayer player) {
				chosenVideoLabel.setText("Video: " + vm.getChosenVideo().getName());
				enablePlayerControl(true);
			}
			
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
		skipTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vp.getPlayer().skip(200 * vp.getSkipDirection());
			}
		});
	}

	protected void createProgressTimer() {
		progressTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long currentTime = vp.getPlayer().getTime();

				progressLabel.setText(TimeFormatter.formatLength(currentTime) + " / " + vp.getVideoLength());
				videoSlider.setValue((int) vp.getCurrentTime());
			}
		});

		progressTimer.start();
	}

	
	// ----------------------- Miscellaneous methods ------------------------

	private void enablePlayerControl(boolean set) {
		videoRWButton.setEnabled(set);
		videoPlayButton.setEnabled(set);
		videoFFButton.setEnabled(set);
	}
	
	private void enableExportButton() {
		if(vm.getChosenVideo() != null && vm.getChosenAudio() != null) {
			videoExportButton.setEnabled(true);
		}
	}
	
	protected void createDir() {
		File vidivoxDir = new File(".vidivox_jmai871");
		vidivoxDir.mkdirs();
	}
}
