package vidivox.worker;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.gui.ExportDialog;
import vidivox.gui.VidivoxGUI;
import vidivox.helper.GenericHelper;

/**
 * OverlayWorker: This class is a SwingWorker that overlays an audio file 
 * onto the audio of a video file and saves the overlaid video as outputName.
 * 
 * Inputs: String videoName, String audioName, String outputName.
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

		String cmd = "ffmpeg -i '" + videoPath + "' ";
		
		for(int i=0; i<audioList.length; i++) {
			cmd += "-i '" + audioList[i] + "' ";
		}
		
		cmd += "-filter_complex \"amix=inputs=" + (audioList.length+1) + "\" \"-shortest\" '" + outputName + "'";
		
		System.out.println(cmd);

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		process = builder.start();
		
		PID = GenericHelper.getPID(process);
		
		process.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		exportDialog.dispose();
		
		deleteOriginal();
		
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
			int returnValue = JOptionPane.showConfirmDialog(exportDialog.getParent(), "Successfully save as \"" + outputName + "\"\n"
					+ "Would you like to play the saved video?", "Export Complete", JOptionPane.YES_NO_OPTION);
			
			if(returnValue == JOptionPane.YES_OPTION) {
				VidivoxGUI.vm.setChosenVideo(new File (outputName));
				VidivoxGUI.vp.playVideo(new File(outputName));
			}
		}
	}
	
	private void deleteOriginal() {
		File vidiTemp = new File(desiredName.getParent() + File.separator + "vidivoxTemp.avi");
		
		if(vidiTemp.exists()) {
			vidiTemp.delete();
		}
	}
	
	private void deleteUnfinished() {
		if(desiredName.exists()) {
			desiredName.delete();
		}
	}
	
}
