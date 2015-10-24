package vidivox.worker;

import java.io.File;
import java.util.ArrayList;

import vidivox.model.AudioTrack;

public class VidivoxWorker {
	public static void saveMp3File(String message, File desiredName) {
		SaveMp3Worker smw = new SaveMp3Worker(message, desiredName);
		smw.execute();
	}
	
	public static void overlay(String videoPath, ArrayList<AudioTrack> audioList, File desiredName) {
		OverlayWorker ow = new OverlayWorker(videoPath, audioList, desiredName);
		ow.execute();
	}

	public static void festival(String text) {
		FestivalWorker fw = new FestivalWorker(text);
		fw.execute();
	}
}
