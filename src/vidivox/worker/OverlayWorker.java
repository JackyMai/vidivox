package vidivox.worker;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidivox.gui.VidivoxGUI;

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
	private String[] audioList;
	private String videoPath;
	private String outputName;
	private File desiredName;
	private JDialog jd;
	
	public OverlayWorker(String videoPath, String[] audioList, File desiredName) {
		this.videoPath = videoPath;
		this.audioList = audioList;
		this.desiredName = desiredName;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		createInfoDialog();
		
		outputName = desiredName.getAbsolutePath();

		String cmd = "ffmpeg -i '" + videoPath + "' ";
		
		for(int i=0; i<audioList.length; i++) {
			cmd += "-i '" + audioList[i] + "' ";
		}
		
		cmd += "-filter_complex 'amix=inputs=" + (audioList.length+1) + "' '" + outputName + "'";
		
		System.out.println(cmd);

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		process.waitFor();
		
		return null;
	}
	
	@Override
	protected void done() {
		jd.dispose();
		
		deleteTemp();
		
		int returnValue = JOptionPane.showConfirmDialog(VidivoxGUI.vp.getPlayerComponent(), "Successfully save as \"" + outputName + "\"\n"
				+ "Would you like to play the saved video?", "Export Complete", JOptionPane.YES_NO_OPTION);
		
		if(returnValue == JOptionPane.YES_OPTION) {
			VidivoxGUI.vm.setChosenVideo(new File (outputName));
			VidivoxGUI.vp.playVideo(new File(outputName));
		}
	}
	
	private void createInfoDialog() {
		JOptionPane jop = new JOptionPane("Exporting the video as requested", 
				JOptionPane.INFORMATION_MESSAGE);
		jd = jop.createDialog(VidivoxGUI.vp.getPlayerComponent(), "Overlay Operation");
		jd.setModal(false);
		jd.setVisible(true);
	}
	
	private void deleteTemp() {
		File vidiTemp = new File(desiredName.getParent() + File.separator + "vidiTemp_JH.avi");
		
		if(vidiTemp.exists()) {
			vidiTemp.delete();
		}
	}
}
