package fr.atesab.atiantengine.utils;

import java.util.regex.Pattern;

class EscapePattern {
	private Pattern pattern;
	private String replacement;

	public EscapePattern(String character, String replacement) {
		this.pattern = Pattern.compile(Pattern.quote("\\" + character));
		this.replacement = replacement;
	}

	String apply(String s) {
		return pattern.matcher(s).replaceAll(replacement);
	}
}
