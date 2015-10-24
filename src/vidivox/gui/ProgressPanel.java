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

public class ProgressPanel extends JPanel {
	private JLabel progressLabel;
	private JSlider videoSlider;
	
	public ProgressPanel(JFrame mainFrame) {
		this.setBackground(Color.decode("#F2F1F0"));
		this.setBorder(new EmptyBorder(6, 9, 2, 9));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		progressLabel = new JLabel("00:00");
		this.add(progressLabel);

		this.add(Box.createRigidArea(new Dimension(5, 0)));

		videoSlider = new JSlider();
		videoSlider.setBackground(Color.decode("#F2F1F0"));
		this.add(videoSlider);

		videoSlider.setMinimum(0);
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
 	
	public void setProgressLabel(String label) {
		this.progressLabel.setText(label);
	}
	
	public void setSliderValue(int value) {
		this.videoSlider.setValue(value);
	}
	
	public void setMaxSliderValue(int value) {
		this.videoSlider.setMaximum(value);
	}
}
