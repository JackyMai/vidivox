package vidivox.worker;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.gui.ExportDialog;
import vidivox.gui.VidivoxGUI;
import vidivox.helper.GenericHelper;

/**
 * This class is a SwingWorker that overlays the audio tracks added by the user
 * onto the video file at the specified insert times and saves the overlaid video
 * as an output.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class OverlayWorker extends SwingWorker<Void, Void> {
	private ExportDialog exportDialog;
	private String[] audioList;
	private String videoPath;
	private String outputName;
	private File desiredName;
	private Process process;
	private int PID;
	
	public OverlayWorker(ExportDialog exportDialog, String videoPath, String[] audioList, File desiredName) {
		this.exportDialog = exportDialog;
		this.videoPath = videoPath;
		this.audioList = audioList;
		this.desiredName = desiredName;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		outputName = desiredName.getAbsolutePath();
		
		// Start concatenating the ffmpeg command for merging the audio files with the video file.
		String cmd = "ffmpeg -i '" + videoPath + "' ";
		
		// Loop through the entire audio list and concatenate the absolute path of the
		// audio files onto the ffmpeg command.
		for(int i=0; i<audioList.length; i++) {
			cmd += "-i '" + audioList[i] + "' ";
		}
		
		// Final concatenation for specifying the number of inputs and the output absolute path.
		cmd += "-filter_complex \"amix=inputs=" + (audioList.length+1) + "\" \"-shortest\" '" + outputName + "'";

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		process = builder.start();
		
		// Obtain the PID of the process in case the user decides to cancel it.
		PID = GenericHelper.getPID(process);
		
		process.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		exportDialog.dispose();
		
		// Deletes the temporary video and audio files if it exists
		deleteOriginal();
		deleteMp3Temp();
		
		// If the user has cancelled the export operation, kill the ffmpeg process and
		// remove the unfinished video file.
		if(this.isCancelled()) {
			String kill = "kill " + PID;
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", kill);
			
			try {
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			deleteUnfinished();
		} else {
			// If the export operation finished normally then ask the user whether they
			// would like to play the exported video.
			int returnValue = JOptionPane.showConfirmDialog(exportDialog.getParent(), 
					"Successfully save as \"" + outputName + "\"\n"
					+ "Would you like to play the exported video?",
					"Export Complete",
					JOptionPane.YES_NO_OPTION);
			
			if(returnValue == JOptionPane.YES_OPTION) {
				VidivoxGUI.vm.setChosenVideo(new File (outputName));
				VidivoxGUI.vp.playVideo(new File(outputName));
			}
		}
	}
	
	/**
	 * Deletes the vidivoxTemp.avi file which is renamed from the original chosen video
	 * when the user decides to overwrite the source video used for the export operation.
	 * Example: current video = helloworld.avi, audio track = hi.mp3, output = helloworld.avi.
	 */
	private void deleteOriginal() {
		File vidiTemp = new File(desiredName.getParent() + File.separator + "vidivoxTemp.avi");
		
		if(vidiTemp.exists()) {
			vidiTemp.delete();
		}
	}
	
	/**
	 * Deletes the unfinished video file when the user cancels the export operation midway.
	 */
	private void deleteUnfinished() {
		if(desiredName.exists()) {
			desiredName.delete();
		}
	}
	
	private void deleteMp3Temp() {
		File tempDir = new File("vidivox" + File.separator + ".temp");
		for(File mp3 : tempDir.listFiles()) {
			mp3.delete();
		}
	}
	
}
