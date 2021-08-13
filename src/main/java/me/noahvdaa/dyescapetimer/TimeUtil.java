package me.noahvdaa.dyescapetimer;

public class TimeUtil {
	public static String format(long millis) {
		int ms = (int) (millis % 1000);
		int sec = (int) (millis / 1000) % 60;
		int min = (int) ((millis / (1000 * 60)) % 60);
		int hr = (int) ((millis / (1000 * 60 * 60)) % 24);

		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
	}
}
