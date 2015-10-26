package vidivox.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import vidivox.helper.GenericHelper;

/**
 * This class is a SwingWorker that takes a string message 
 * and plays it via a festival bash call
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class FestivalWorker extends SwingWorker<Void, Void> {
	private String message;
	private int PID;
	
	public FestivalWorker(String message) {
		this.message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// Creates a process to make festival speak the message provided
		String cmd = "echo '" + message + "' | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		
		// Obtain the PID of the process in case the user decides to cancel it.
		PID = GenericHelper.getPID(process);
		
		process.waitFor();
		
		return null;
	}
	
	protected void done() {
		// If the FestivalWorker got cancelled by the user then kill the aplay process
		// from Festival to end the voice.
		if(this.isCancelled()) {
			String cmd = "pstree -p " + PID;
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			
			Process process;
			try {
				// Use the PID of the Festival process to obtain the PID of the aplay process to kill it.
				process = builder.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = br.readLine();
				
				if(line != null) {
					int aplayIndex = line.indexOf("play(") + 5;
					String aplayPID = line.substring(aplayIndex, line.indexOf(")", aplayIndex));
					
					String killcmd = "kill " + aplayPID;
					ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", killcmd);
					builder2.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
