package vidivox.worker;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.gui.VidivoxGUI;

/* SaveMp3Worker: This class is a SwingWorker that saves a message to a temporary
 * wav output using bash, then converts the wav output to mp3 through piping.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class SaveMp3Worker extends SwingWorker<Void, Void> {
	private String outputName;
	private String message;
	
	/**
	 * Constructor that stores the input arguments into the private fields.
	 * @param message - the message that the user wants to convert to mp3
	 * @param desiredName - contains the absolute path for the output destination
	 */
	public SaveMp3Worker(String message, File desiredName) {
		this.message = message;
		this.outputName = desiredName.getAbsolutePath();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// Echo the message to text2wave then use piping again to pass it to ffmpeg for conversion
		String save = "echo '" + message + "' | text2wave | ffmpeg -i - '" + outputName + "'";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", save);
		builder.start();
		
		return null;
	}
	
	@Override
	protected void done() {
		JOptionPane.showMessageDialog(VidivoxGUI.vp.getPlayerComponent(), "Successfully saved as '" + outputName + "'");
	}
}