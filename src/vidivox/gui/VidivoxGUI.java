package vidivox.gui;

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
import vidivox.helper.GenericHelper;
import vidivox.helper.TextLimit;
import vidivox.helper.TimeFormatter;
import vidivox.model.VidivoxModel;
import vidivox.model.VidivoxPlayer;
import vidivox.worker.VidivoxWorker;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.table.DefaultTableModel;

import java.awt.FlowLayout;
import java.awt.Insets;
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
	protected static Timer progressTimer;
	protected static Timer skipTimer;
	private JPanel topPanel;
	private ProgressPanel progressPanel;
	private PlayerControlPanel playerControlPanel;
	private StatusPanel statusPanel = new StatusPanel(mainFrame);
	private AudioFestivalPanel audioFestivalPanel;
	private JTextField commentTextField;
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
		
		GenericHelper.createDir();
		
		// ------------------------ Top Panel ----------------------------------

		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		mainFrame.add(topPanel);
		
		// -------------------------- Player Panel -----------------------------
		
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout(0,0));
		playerPanel.add(vp.getPlayerComponent(), BorderLayout.CENTER);
		topPanel.add(playerPanel);
		
		
		// ------------------------ Progress Panel ------------------------------
		
		progressPanel = new ProgressPanel(mainFrame);
		topPanel.add(progressPanel);

		
		// ---------------------- Controller Panel -----------------------------
		
		playerControlPanel = new PlayerControlPanel(mainFrame, statusPanel);
		topPanel.add(playerControlPanel);
		

		// ------------------------- Audio Track Panel ---------------------------
		
		JPanel audioTrackPanel = new JPanel();
		audioTrackPanel.setLayout(new BoxLayout(audioTrackPanel, BoxLayout.Y_AXIS));
		topPanel.add(audioTrackPanel);
		
		
		// --------------------------- Comment Input Panel ----------------------------
		
		audioFestivalPanel = new AudioFestivalPanel(mainFrame);
		audioTrackPanel.add(audioFestivalPanel);

		
		// -------------------------- Comment Table Panel -------------------------------
		
		JPanel commentTablePanel = new JPanel();
		commentTablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 6));
		audioTrackPanel.add(commentTablePanel);
		
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
		commentScrollPane.setPreferredSize(new Dimension(audioFestivalPanel.getPreferredSize().width-4, 86));
		commentTablePanel.add(commentScrollPane);
		
		
		// ------------------- Status Panel -----------------------------
		
		topPanel.add(statusPanel);

		
		// ------------------------- Player Setup ---------------------------

		vp.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void lengthChanged(MediaPlayer player, long newTime) {
				progressPanel.setMaxSliderValue((int) newTime);
				
				String videoLength = TimeFormatter.formatLength(newTime);
				vp.setVideoLength(videoLength);
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
				
				progressPanel.setProgressLabel(TimeFormatter.formatLength(currentTime) + " / " + vp.getVideoLength());
				progressPanel.setSliderValue((int) vp.getCurrentTime());
			}
		});

		progressTimer.start();
	}
}
