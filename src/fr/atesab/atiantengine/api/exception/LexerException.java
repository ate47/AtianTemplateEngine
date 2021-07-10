package fr.atesab.atiantengine.api.exception;

public class LexerException extends RuntimeException {
	private static String formatLineCharException(String msg, int line, int character) {
		return msg + " (" + line + ":" + character + ")";
	}

	public LexerException(String msg, int line, int character) {
		super(formatLineCharException(msg, line, character));
	}

	public LexerException(String msg, Throwable throwable, int line, int character) {
		super(formatLineCharException(msg, line, character), throwable);
	}
}
