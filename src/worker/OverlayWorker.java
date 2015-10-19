package worker;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.VidivoxPlayer;

/*
 * OverlayWorker: This class is a SwingWorker that overlays an audio file 
 * onto the audio of a video file and saves the overlayed video as outputName
 * 
 * Inputs: String videoName, String audioName, String outputName
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */
public class OverlayWorker extends SwingWorker<Void, Void> {
	private String videoPath;
	private String audioPath;
	private String outputName;
	private File desiredName;
	private VidivoxPlayer vp;
	private JDialog jd;
	
	public OverlayWorker(String videoPath, String audioPath, File desiredName, VidivoxPlayer vp) {
		this.videoPath = videoPath;
		this.audioPath = audioPath;
		this.desiredName = desiredName;
		this.vp = vp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		JOptionPane jop = new JOptionPane("Processing the overlay as requested...", 
				JOptionPane.INFORMATION_MESSAGE);
		jd = jop.createDialog(vp.getPlayerComponent(), "Overlay operation");
		jd.setModal(false);
		jd.setVisible(true);
		
		outputName = desiredName.getAbsolutePath();

		String cmd = "ffmpeg -i '" + videoPath + "' -i '" + audioPath + 
				"' -filter_complex 'amix=inputs=2:duration=first:dropout_transition=2' '" + outputName + "'";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		process.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		jd.setVisible(false);
		
		File vidiTemp = new File(desiredName.getParent() + File.separator + "vidiTemp_JH.avi");
		
		if(vidiTemp.exists()) {
			vidiTemp.delete();
		}
		
		JOptionPane.showMessageDialog(vp.getPlayerComponent(), "Successfully saved as \"" + outputName + "\"");
		vp.setChosenVideo(new File (outputName));
		vp.playVideo();
	}
}
