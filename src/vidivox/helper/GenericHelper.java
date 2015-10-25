package vidivox.helper;

import java.io.File;

import vidivox.gui.VidivoxGUI;

public class GenericHelper {
	public static void createDir() {
		File vidivoxDir = new File("vidivox" + File.separator + ".temp");
		vidivoxDir.mkdirs();
	}
	
	public static boolean checkValidInsertTime(String newTime) {
		int videoLength = VidivoxGUI.vp.getVideoDuration();
		int newTimeInMilli = TimeFormatter.stringToMilli(newTime);
		
		if(newTimeInMilli >= 0 && newTimeInMilli <= videoLength) {
			return true;
		}
		
		return false;
	}
}
