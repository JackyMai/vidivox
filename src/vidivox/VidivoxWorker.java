package vidivox;

import java.io.File;
import vidivox.worker.FestivalWorker;
import vidivox.worker.OverlayWorker;
import vidivox.worker.SaveMp3Worker;

public class VidivoxWorker {
	public static void saveMp3File(String message, File desiredName, VidivoxPlayer vp) {
		SaveMp3Worker smw = new SaveMp3Worker(message, desiredName, vp);
		smw.execute();
	}
	
	public static void overlay(String videoPath, String audioPath, File desiredName, VidivoxPlayer vp) {
		OverlayWorker ow = new OverlayWorker(videoPath, audioPath, desiredName, vp);
		ow.execute();
	}

	public static void festival(String text) {
		FestivalWorker fw = new FestivalWorker(text);
		fw.execute();
	}
}
