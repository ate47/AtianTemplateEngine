package fr.atesab.atiantengine.utils;

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
}
