package worker;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.VidivoxPlayer;

/* SaveMp3Worker: This class is a SwingWorker that saves a message to temporary .wav 
 * using BASH calls from process builder, then converts the wav files to mp3
 * 
 * Inputs: String message, File desiredName
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */
public class SaveMp3Worker extends SwingWorker<Void, Void> {
	private String outputName;
	private String message;
	private String parentDir;
	private VidivoxPlayer vp;
	private JDialog jd;
	private String tempName = "vidiTemp_JH.wav";
	
	public SaveMp3Worker(String message, File desiredName, VidivoxPlayer vp) {
		this.message = message;
		this.outputName = desiredName.getAbsolutePath();
		this.parentDir = desiredName.getParent();
		this.vp = vp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		JOptionPane jop = new JOptionPane("Saving message to mp3 file as requested...", 
				JOptionPane.INFORMATION_MESSAGE);
		jd = jop.createDialog(vp.getPlayerComponent(), "Saving operation");
		jd.setModal(false);
		jd.setVisible(true);
		
		String save = "echo \"" + message + "\" | text2wave -o \"" + parentDir + File.separator + tempName + "\";";
		String convert = "ffmpeg -i \"" + parentDir + File.separator + tempName + "\" \"" + outputName + "\"";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", save + convert);
		
		Process process = builder.start();
		process.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		String cmd = "rm \"" + parentDir + File.separator + tempName + "\"";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
	
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		jd.setVisible(false);
		
		JOptionPane.showMessageDialog(vp.getPlayerComponent(), "Successfully saved as \"" + outputName + "\"");
	}
}