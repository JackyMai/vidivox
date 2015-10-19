package worker;
import javax.swing.SwingWorker;

/* 
 * FestivalWorker: This class is a SwingWorker that takes a string message 
 * and plays it via a festival BASH call
 * 
 * Inputs: String message
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */

public class FestivalWorker extends SwingWorker<Void, Void> {
	private String message;
	
	public FestivalWorker(String message) {
		this.message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		String cmd = "echo '" + message + "' | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder.start();
		
		return null;
	}

}
