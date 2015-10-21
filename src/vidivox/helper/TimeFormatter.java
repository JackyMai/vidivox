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
}
