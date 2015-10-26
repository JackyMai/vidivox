package vidivox.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import vidivox.helper.GenericHelper;
import vidivox.helper.TimeFormatter;
import vidivox.model.VidivoxModel;
import vidivox.model.VidivoxPlayer;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/**
 * This class deals with the GUI aspect of the entire Vidivox program.
 * But rather than showing all the details it only describes the main structure
 * and layout of the various panels.
 * 
 * Authors Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class VidivoxGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JFrame mainFrame = this;
	private Dimension vidivoxDim = new Dimension(900, 760);
	public static VidivoxPlayer vp;
	public static VidivoxModel vm;
	protected static Timer progressTimer;
	protected static Timer skipTimer;
	private JPanel topPanel;
	private ProgressPanel progressPanel;
	private PlayerControlPanel playerControlPanel;
	private AudioPanel audioPanel;
	private StatusPanel statusPanel = new StatusPanel(mainFrame);
	
	// -------------------------- Constructor --------------------------

	public VidivoxGUI() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("VIDIVOX");
		setSize(vidivoxDim);
		setLocationRelativeTo(null);
		setResizable(false);
		
		/*
		 * Adds a WindowListener to the JFrame.
		 * Instead of exiting immediately when the user tries to close the window,
		 * the user will be greeted with a JOptionPane asking for confirmation.
		 */
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String buttons[] = {"Yes", "No"};
				int returnValue = JOptionPane.showOptionDialog(mainFrame, "Are you sure that you want to exit Vidivox?\n"
						+ "Existing audio tracks will need to be re-added.",
						"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, buttons, buttons[1]);
				 
				if(returnValue == 0) {
					System.exit(0);
				}
			}
		});
		
		vp = new VidivoxPlayer();
		vm = new VidivoxModel();
		
		GenericHelper.createDir();
		
		
		// -------------------------- Top Panel --------------------------

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		mainFrame.add(topPanel);
		
		
		// -------------------------- Player Panel --------------------------
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout(0,0));
		playerPanel.add(vp.getPlayerComponent(), BorderLayout.CENTER);
		playerPanel.setPreferredSize(new Dimension(vidivoxDim.width, 500));
		topPanel.add(playerPanel);
		
		
		// -------------------------- Progress Panel --------------------------
		
		progressPanel = new ProgressPanel(mainFrame);
		topPanel.add(progressPanel);

		
		// -------------------------- Controller Panel --------------------------
		
		playerControlPanel = new PlayerControlPanel(mainFrame, statusPanel);
		topPanel.add(playerControlPanel);
		
		
		// -------------------------- Audio Panel --------------------------
		
		audioPanel = new AudioPanel(mainFrame, statusPanel, playerControlPanel);
		topPanel.add(audioPanel);
		
		
		// -------------------------- Status Panel --------------------------
		
		topPanel.add(statusPanel);

		
		// -------------------------- Player Setup --------------------------
		
		// Adds a listener for the media player to notify for various events.
		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			// Updates the maximum value of the video slider when the length of the
			// video has changed, such as opening a new video.
			@Override
			public void lengthChanged(MediaPlayer player, long newTime) {
				progressPanel.setMaxSliderValue((int) newTime);
				
				vp.setVideoDuration((int)newTime);
			}
			
			// Updates the chosen video label on the status panel to the latest video file.
			@Override
			public void opening(MediaPlayer player) {
				statusPanel.setChosenVideoLabel("Video: " + vm.getChosenVideo().getName());
				playerControlPanel.setPlayStatus();
				playerControlPanel.enablePlayerControl(true);
			}
			
			// Updates the various GUI elements when the video finishes playing.
			@Override
			public void finished(MediaPlayer player) {
				playerControlPanel.setStoppedStatus();
				vp.stop();
			}
		});

		createProgressTimer();
		createSkipTimer();

		mainFrame.setVisible(true);
	}
	
	
	// -------------------------- Timer Creation Methods --------------------------
	
	/**
	 * Creates a timer for the fast-forward and rewind functionality of the player.
	 */
	protected void createSkipTimer() {
		skipTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vp.getPlayer().skip(200 * vp.getSkipDirection());
			}
		});
	}
	
	/**
	 * Creates a timer for updating the video slider and video label for the player
	 * depending on the current position in the video.
	 */
	protected void createProgressTimer() {
		progressTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long currentTime = vp.getPlayer().getTime();
				
				progressPanel.setProgressLabel(TimeFormatter.milliToString(currentTime) + " / " + vp.getVideoLength());
				progressPanel.setSliderValue((int) vp.getCurrentTime());
			}
		});

		progressTimer.start();
	}
}
