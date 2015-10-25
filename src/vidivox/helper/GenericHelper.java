package vidivox.helper;

import java.io.File;

import vidivox.gui.VidivoxGUI;

public class GenericHelper {
	public static void createDir() {
		File vidivoxDir = new File("vidivox" + File.separator + ".temp");
		vidivoxDir.mkdirs();
	}
	
	public static boolean checkValidInsertTime(String newTime) {
		String minTimePattern = "([0-9]{0,2}):([0-5])([0-9])";
		String hourTimePattern = minTimePattern + ":([0-5])([0-9])";
		
		String[] newLength = newTime.split(":");
		String[] matchColon = VidivoxGUI.vp.getVideoLength().split(":");
		
		int videoLength = VidivoxGUI.vp.getVideoDuration();
		
		if(newLength.length == matchColon.length && 
			(newTime.matches(minTimePattern) || newTime.matches(hourTimePattern))) {
				int newTimeInMilli = TimeFormatter.stringToMilli(newTime);
				
				if(newTimeInMilli >= 0 && newTimeInMilli <= videoLength) {
					return true;
				}
			}
		
		return false;
	}
}
