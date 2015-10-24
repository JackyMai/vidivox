package vidivox.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;

import vidivox.filechooser.FileOpener;
import vidivox.filechooser.FileSaver;
import vidivox.model.VidivoxPlayer;

public class PlayerControlPanel extends JPanel {
	private StatusPanel statusPanel;
	private JButton browseButton;
	private JButton rewindButton;
	private JButton playButton;
	private JButton ffButton;
	private JButton exportButton;
	private JSlider volumeSlider;
	
	public PlayerControlPanel(final JFrame mainFrame, StatusPanel statusPanel) {
		this.statusPanel = statusPanel;
		
		this.setBackground(Color.decode("#F2F1F0"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));

		browseButton = new JButton("Browse");
		browseButton.setMargin(new Insets(2,6,2,6));
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(FileOpener.openVideo(mainFrame)) {
					setPlayStatus();
					enableExportButton();
				}
			}
		});
		this.add(browseButton);

		rewindButton = new JButton("<<");
		rewindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRewindStatus();
				VidivoxGUI.vp.skip(-1);
			}
		});
		this.add(rewindButton);

		playButton = new JButton("▐ ▌");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (VidivoxPlayer.getVideoPaused() == 0) {
					setPauseStatus();
					VidivoxGUI.vp.pause();
				} else {
					setPlayStatus();
					VidivoxGUI.vp.play();
				}
			}
		});
		this.add(playButton);

		ffButton = new JButton(">>");
		ffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFastForwardStatus();
				VidivoxGUI.vp.skip(1);
			}
		});
		this.add(ffButton);
		
		enablePlayerControl(false);
		
		int defaultVolume = VidivoxGUI.vp.getDefaultVolume();
		
		volumeSlider = new JSlider(JSlider.VERTICAL, 0, 150, defaultVolume);
		volumeSlider.setToolTipText(String.valueOf(defaultVolume));
		volumeSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int volume = volumeSlider.getValue();
				VidivoxGUI.vp.getPlayer().setVolume(volume);
				volumeSlider.setToolTipText(String.valueOf(volume));
			}
		});

		final JPopupMenu volumeMenu = new JPopupMenu();
		volumeMenu.add(volumeSlider);

		final JButton volumeButton = new JButton("Volume");
		volumeButton.setMargin(new Insets(2,6,2,6));
		volumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				volumeMenu.show(volumeButton, volumeButton.getBounds().x - 200,
						volumeButton.getBounds().y - 215);
			}
		});
		this.add(volumeButton);

		exportButton = new JButton("Export");
		exportButton.setMargin(new Insets(2,6,2,6));
		exportButton.setEnabled(false);
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileSaver.exportFile(mainFrame, "video", null);
			}
		});
		this.add(exportButton);

	}
	
	public int getVolumeSliderValue() {
		return volumeSlider.getValue();
	}
	
	public void setPlayStatus() {
		VidivoxGUI.skipTimer.stop();
		playButton.setText("▐ ▌");
		statusPanel.setPlayerStatusLabel("Status: playing");
	}

	protected void setPauseStatus() {
		VidivoxGUI.skipTimer.stop();
		playButton.setText(" ▶ ");
		statusPanel.setPlayerStatusLabel("Status: paused");
	}

	protected void setStoppedStatus() {
		VidivoxGUI.skipTimer.stop();
		playButton.setText(" ▶ ");
		statusPanel.setPlayerStatusLabel("Status: stopped");
	}

	protected void setFastForwardStatus() {
		VidivoxGUI.skipTimer.start();
		playButton.setText(" ▶ ");
		statusPanel.setPlayerStatusLabel("Status: fast-forward");
	}

	protected void setRewindStatus() {
		VidivoxGUI.skipTimer.start();
		playButton.setText(" ▶ ");
		statusPanel.setPlayerStatusLabel("Status: rewind");
	}
	
	protected void enablePlayerControl(boolean set) {
		this.rewindButton.setEnabled(set);
		this.playButton.setEnabled(set);
		this.ffButton.setEnabled(set);
	}
	
	protected void enableExportButton() {
		if(VidivoxGUI.vm.getChosenVideo() != null && VidivoxGUI.vm.getChosenAudio() != null) {
			exportButton.setEnabled(true);
		}
	}
	
//	final JPopupMenu openMenu = new JPopupMenu();

//	JMenuItem openVideoMenuItem = new JMenuItem("Video file");
//	JMenuItem openAudioMenuItem = new JMenuItem("Audio track");
//	openMenu.add(openVideoMenuItem);
//	openMenu.add(openAudioMenuItem);

//	browseBtn.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//			openMenu.show(browseBtn, browseBtn.getBounds().x,
//					browseBtn.getBounds().y + browseBtn.getHeight());
//		}
//	});

//	openAudioMenuItem.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//			if(FileOpener.openAudio(mainFrame)) {
////				chosenAudioLabel.setText("Audio track: " + vp.getChosenAudio().getName());
//				AudioTrack newTrack = new AudioTrack(vm.getChosenAudio(), (int)vp.getCurrentTime());
//				commentModel.addRow(new Object[]{newTrack.getAudioName(), TimeFormatter.formatLength(newTrack.getInsertTime())});
//				vm.addAudioTrack(newTrack);
//				
//				enableExportButton();
//			}
//		}
//	});
}