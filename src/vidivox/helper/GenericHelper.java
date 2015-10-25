package vidivox.helper;

import java.io.File;
import java.lang.reflect.Field;

import vidivox.gui.VidivoxGUI;

/**
 * This class contains all the miscellaneous helper methods used to assist
 * the Vidivox functionality.
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class GenericHelper {
	/**
	 * Creates the necessary directories for Vidivox
	 */
	public static void createDir() {
		File vidivoxDir = new File("vidivox" + File.separator + ".temp");
		vidivoxDir.mkdirs();
	}
	
	/**
	 * Checks whether the entered insert time is valid for the chosen video.
	 * 
	 * @param newTime - the new insert time the user has entered
	 * @return true if newTime is considered a valid insert for the video
	 */
	public static boolean checkValidInsertTime(String newTime) {
		// Regex pattern that the newTime should match depending on whether
		// the video is only one hour long. The basic pattern is (HH:)MM:SS.
		String minTimePattern = "([0-9]{0,2}):([0-5])([0-9])";
		String hourTimePattern = minTimePattern + ":([0-5])([0-9])";
		
		// Finds out how many colons there are in the new time as well as
		// the number of colons for the current video length.
		String[] newLength = newTime.split(":");
		String[] matchColon = VidivoxGUI.vp.getVideoLength().split(":");
		
		int videoLength = VidivoxGUI.vp.getVideoDuration();
		
		// The pattern of the newTime string must equal to either one of the
		// HH:MM:SS or MM:SS pattern depending on the length of the chosen video.
		if(newLength.length == matchColon.length && 
			(newTime.matches(minTimePattern) || newTime.matches(hourTimePattern))) {
				int newTimeInMilli = TimeFormatter.stringToMilli(newTime);
				
				// The new insert time must also be between 0 and the length of the video
				if(newTimeInMilli >= 0 && newTimeInMilli <= videoLength) {
					return true;
				}
			}
		
		return false;
	}
	
	/**
	 * This method is the hack that will return the PID of a UNIX process.
	 * @param process - the process that you want to obtain the PID for
	 * @return PID of the UNIX process
	 */
	public static int getPID(Process process) {
		if(process.getClass().getName().equals("java.lang.UNIXProcess")) {
			Field f = null;
			int pid = 0;

			try {
				f = process.getClass().getDeclaredField("pid");
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}

			f.setAccessible(true);
			
			try {
				pid = f.getInt(process);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return pid;
		}
		
		return -1;
	}
}
