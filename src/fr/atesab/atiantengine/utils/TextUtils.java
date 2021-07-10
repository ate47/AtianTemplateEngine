package fr.atesab.atiantengine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class TextUtils {
	private TextUtils() {
		throw new Error("Can't init util class");
	}

	private static final EscapePattern[] ESCAPE_PATTERNS = { new EscapePattern("n", "\n"), new EscapePattern("r", "\r"),
			new EscapePattern("t", "\t"), new EscapePattern("b", "\b"), new EscapePattern("f", "\f"),
			new EscapePattern("\\", "\\") };

	public static String escape(String string) {
		String s = string;

		for (EscapePattern escapePattern : ESCAPE_PATTERNS) {
			s = escapePattern.apply(s);
		}

		return s;
	}

	public static String readInput(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append("\n").append(line);
		}
		String s = buffer.toString();
		if (!s.isEmpty()) {
			s = s.substring(1);
		}
		return s;
	}
}
