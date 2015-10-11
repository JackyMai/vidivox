package vidivox;


import java.io.File;

import worker.FestivalWorker;
import worker.OverlayWorker;
import worker.SaveMp3Worker;

public class VidivoxWorker {
	public void saveMp3File(String message, File desiredName, VidivoxPlayer vp) {
		SaveMp3Worker smw = new SaveMp3Worker(message, desiredName, vp);
		smw.execute();
	}

	public void overlay(String videoPath, String audioPath, File desiredName, VidivoxPlayer vp) {
		OverlayWorker ow = new OverlayWorker(videoPath, audioPath, desiredName, vp);
		ow.execute();
	}

	public void festival(String text) {
		FestivalWorker fw = new FestivalWorker(text);
		fw.execute();
	}
}
