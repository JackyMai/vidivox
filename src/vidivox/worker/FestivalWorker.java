package vidivox.worker;

import javax.swing.SwingWorker;

/**
 * This class is a SwingWorker that takes a string message 
 * and plays it via a festival BASH call
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class FestivalWorker extends SwingWorker<Void, Void> {
	private String message;
	
	public FestivalWorker(String message) {
		this.message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// Creates a process to make festival speak the message provided
		String cmd = "echo '" + message + "' | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder.start();
		
		return null;
	}
}
