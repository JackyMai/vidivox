package vidivox.helper;

public class TimeFormatter {
	/**
	 * formatLength: This method takes a long number video length and
	 * converts it into a readable string
	 */
	public static String formatLength(long videoLength) {
		int totalLength = (int) videoLength / 1000;
		int min = totalLength / 60;
		int sec = totalLength % 60;

		if (min < 60) {
			return String.format("%02d:%02d", min, sec);
		} else {
			int hour = min / 60;
			return String.format("%02d:%02d:%02d", hour, min - 60, sec);
		}
	}
	
	public static int stringToMilli(String time) {
		String[] parts = time.split(":");
		
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
		
		return 0;
	}
}
