package vidivox.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import vidivox.gui.VidivoxGUI;
import vidivox.model.AudioTrack;

public class DelayWorker extends SwingWorker<String[], Void> {
	private ArrayList<AudioTrack> audioList;
	
	public DelayWorker(ArrayList<AudioTrack> audioList) {
		this.audioList = audioList;
	}
	
	@Override
	protected String[] doInBackground() throws Exception {
		String[] delayedAudioList = new String[audioList.size()];
		
		// Loop through every single AudioTrack objects in the ArrayList and create a temporary copy of the
		// audio files with the delay time specified using ffmpeg's adelay.
		for(int i=0; i<audioList.size(); i++) {
			// Getting a new AudioTrack object on every iteration and setting up the necessary variables
			AudioTrack audioTrack = audioList.get(i);
			int insertTime = audioTrack.getInsertTime();
			String outputName = "vidivox" + File.separator + ".temp" + File.separator + audioTrack.getAudioName();
			
			// Concatenate the ffmpeg command depending on the condition of the delay time
			String cmd = "ffmpeg -y -i '" + audioTrack.getAudioPath() + "' -filter_complex \"adelay=";
			
			// If the delay time is greater than 1, create a temporary delayed copy
			// Else, use the path of the original audio file
			if(insertTime > 1) {
				cmd += insertTime + "|" + insertTime + "|" + insertTime;
				delayedAudioList[i] = outputName;
			} else {
				delayedAudioList[i] = audioTrack.getAudioPath();
			}
			
			cmd += "\" '" + outputName + "'";
			
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = builder.start();
			process.waitFor();
		}
		
		return delayedAudioList;
	}
	
	protected void done() {
		try {
			VidivoxGUI.vm.setDelayedAudioList(get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}