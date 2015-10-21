package vidivox.worker;
import java.io.File;
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
	private VidivoxPlayer vp;
	
	public SaveMp3Worker(String message, File desiredName, VidivoxPlayer vp) {
		this.message = message;
		this.outputName = desiredName.getAbsolutePath();
		this.vp = vp;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		String save = "echo '" + message + "' | text2wave | ffmpeg -i - '" + outputName + "'";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", save);
		builder.start();
		
		return null;
	}
	
	@Override
	protected void done() {
		JOptionPane.showMessageDialog(vp.getPlayerComponent(), "Successfully saved as '" + outputName + "'");
	}
}