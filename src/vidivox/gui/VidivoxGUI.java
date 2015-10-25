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

/* VideoPlayerGUI: This class deals with the GUI aspect of the video player from BigBuckPlayer 
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
	
	// -------- Constructor: creates the frame, panels, buttons, etc. ---------

	public VidivoxGUI() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("VIDIVOX");
		setSize(vidivoxDim);
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
		
		GenericHelper.createDir();
		
		// ------------------------ Top Panel ----------------------------------

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		mainFrame.add(topPanel);
		
		// -------------------------- Player Panel -----------------------------
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout(0,0));
		playerPanel.add(vp.getPlayerComponent(), BorderLayout.CENTER);
		playerPanel.setPreferredSize(new Dimension(vidivoxDim.width, 500));
		topPanel.add(playerPanel);
		
		
		// ------------------------ Progress Panel ------------------------------
		
		progressPanel = new ProgressPanel(mainFrame);
		topPanel.add(progressPanel);

		
		// ---------------------- Controller Panel -----------------------------
		
		playerControlPanel = new PlayerControlPanel(mainFrame, statusPanel);
		topPanel.add(playerControlPanel);
		
		
		// -------------------------- Audio Panel --------------------------
		
		audioPanel = new AudioPanel(mainFrame, statusPanel, playerControlPanel);
		topPanel.add(audioPanel);
		
		// ------------------- Status Panel -----------------------------
		
		topPanel.add(statusPanel);

		
		// ------------------------- Player Setup ---------------------------

		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void lengthChanged(MediaPlayer player, long newTime) {
				progressPanel.setMaxSliderValue((int) newTime);
				
				vp.setVideoDuration((int)newTime);
			}
			
			@Override
			public void opening(MediaPlayer player) {
				statusPanel.setChosenVideoLabel("Video: " + vm.getChosenVideo().getName());
				playerControlPanel.enablePlayerControl(true);
			}
			
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
				
				progressPanel.setProgressLabel(TimeFormatter.milliToString(currentTime) + " / " + vp.getVideoLength());
				progressPanel.setSliderValue((int) vp.getCurrentTime());
			}
		});

		progressTimer.start();
	}
}
