package vidivox.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalSliderUI;

/**
 * This class is a JPanel that contains the progress label and video
 * slider for the video player component. 
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class ProgressPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel progressLabel;
	private JSlider videoSlider;
	
	public ProgressPanel(JFrame mainFrame) {
		this.setBackground(Color.decode("#F3F3F3"));
		this.setBorder(new EmptyBorder(6, 10, 3, 6));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// -------------------------- Progress Label --------------------------
		// The progress label shows the current play time over the total video
		// length. 
		
		progressLabel = new JLabel("00:00 / 00:00");
		this.add(progressLabel);

		this.add(Box.createRigidArea(new Dimension(5, 0)));
		
		
		// -------------------------- Video Slider --------------------------
		// The video slider allows the user to drag the slider thumb to quickly
		// skip to any part of the video they want. Clicking on the slider track
		// has the same effect like most video players.
		
		videoSlider = new JSlider();
		videoSlider.setBackground(Color.decode("#F2F1F0"));
		this.add(videoSlider);

		videoSlider.setMinimum(0);
		
		// Overrides the scrollDueToClickInTrack method to allow the user to skip
		// by clicking on the slider track.
		// Reference: http://stackoverflow.com/a/518672
		videoSlider.setUI(new MetalSliderUI() {
			@Override
			protected void scrollDueToClickInTrack(int direction) {
		        int value = this.valueForXPosition(videoSlider.getMousePosition().x);
		        VidivoxGUI.vp.getPlayer().setTime(value);
		    }
		});
		videoSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				VidivoxGUI.vp.getPlayer().setTime(videoSlider.getValue());
			}
		});
	}
 	
	/**
	 * Sets the text on the progress label to the provided string
	 * @param label - a string that shows the new progress time
	 */
	public void setProgressLabel(String label) {
		this.progressLabel.setText(label);
	}
	
	/**
	 * Sets the slider value of the video slider to allow the thumb
	 * to move across the track.
	 * @param value - the new position of the thumb
	 */
	public void setSliderValue(int value) {
		this.videoSlider.setValue(value);
	}
	
	/**
	 * Sets the maximum value of the video slider.
	 * Used in situations such as opening a new video file.
	 * @param value - the new maximum value of the slider
	 */
	public void setMaxSliderValue(int value) {
		this.videoSlider.setMaximum(value);
	}
}
