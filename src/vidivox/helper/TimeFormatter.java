package vidivox.helper;

public class TimeFormatter {
	/**
	 * This method takes a long number video length and converts
	 * it into a readable string in the format (HH:)MM:SS.
	 */
	public static String milliToString(long videoLength) {
		int totalLength = (int) videoLength / 1000;
		int min = totalLength / 60;
		int sec = totalLength % 60;
		
		// If the video length is less than an hour, then return the string
		// in the MM:SS format, else, return in the HH:MM:SS format.
		if (min < 60) {
			return String.format("%02d:%02d", min, sec);
		} else {
			int hour = min / 60;
			return String.format("%02d:%02d:%02d", hour, min - 60, sec);
		}
	}
	
	/**
	 * This method takes a time string in the format (HH:)MM:SS and
	 * converts it to an integer value in the millisecond unit.
	 * @param time
	 * @return
	 */
	public static int stringToMilli(String time) {
		// Split the time string by ":" to decide whether the string
		// is in the HH:MM:SS format or the MM:SS format.
		String[] parts = time.split(":");
		
		// Calculate the time in millisecond depending on whether the
		// input time is over an hour long.
		if(parts.length == 3){
			int hour = Integer.parseInt(parts[0]);
			int min = Integer.parseInt(parts[1]);
			int sec = Integer.parseInt(parts[2]);
			int milli = (sec + min*60 + hour*3600) * 1000;
			
			return milli;
		} else if(parts.length == 2) {
			int min = Integer.parseInt(parts[0]);
			int sec = Integer.parseInt(parts[1]);
			int milli = (sec + min*60) * 1000;
			
			return milli;
		}
		
		return -1;
	}
}
