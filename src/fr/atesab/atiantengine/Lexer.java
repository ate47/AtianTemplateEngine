package fr.atesab.atiantengine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.atesab.atiantengine.api.exception.LexerException;
import fr.atesab.atiantengine.api.lexer.ILexeme;
import fr.atesab.atiantengine.api.lexer.ILexemeBuilder;
import fr.atesab.atiantengine.api.lexer.ILexemeStream;
import fr.atesab.atiantengine.api.lexer.IPartialLexemeFind;
import fr.atesab.atiantengine.impl.lexer.Lexeme;
import fr.atesab.atiantengine.impl.lexer.LexemeStream;

public class Lexer<T extends ILexeme> {
	private final class PartialLexemeFind implements Comparable<Lexer<T>.PartialLexemeFind>, IPartialLexemeFind<T> {
		int right;
		T lexeme;
		Matcher matcher;

		PartialLexemeFind(int right, T lexeme, Matcher matcher) {
			this.right = right;
			this.lexeme = lexeme;
			this.matcher = matcher;
		}

		@Override
		public int getRight() {
			return right;
		}

		@Override
		public T getLexeme() {
			return lexeme;
		}

		@Override
		public Matcher getMatcher() {
			return matcher;
		}

		@Override
		public int compareTo(Lexer<T>.PartialLexemeFind o) {
			return right - o.right;
		}

	}

	public static final Pattern LEXEME_NAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z_0-9]*");
	public static final char CONST_STRING_CHAR = '"';

	public static Lexer<ILexeme> buildFromFile(String file) throws IOException {
		try (InputStream is = new FileInputStream(file)) {
			return buildFromInput(is, Lexeme::new);
		}
	}

	public static <T extends ILexeme> Lexer<T> buildFromFile(String file, ILexemeBuilder<T> builder)
			throws IOException {
		try (InputStream is = new FileInputStream(file)) {
			return buildFromInput(is, builder);
		}
	}

	public static Lexer<ILexeme> buildFromInput(InputStream stream) throws IOException {
		return buildFromInput(stream, Lexeme::new);
	}

	public static <T extends ILexeme> Lexer<T> buildFromInput(InputStream stream, ILexemeBuilder<T> builder)
			throws IOException {
		Lexer<T> lexer = new Lexer<>();
		try (BufferedReader r = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			int lineCount = 0;
			while ((line = r.readLine()) != null) {
				lineCount++;
				lexer.readLexerConfigLine(line, lineCount, builder);
			}
		}
		return lexer;
	}

	private Map<String, T> lexemesMap = new HashMap<>();

	public T findLexemeByIdentifier(String idf) {
		return lexemesMap.get(idf);
	}

	private void readLexerConfigLine(String line, int lineCount, ILexemeBuilder<T> builder) {
		if (line == null)
			return;
		int left = 0;
		while (left < line.length() && Character.isWhitespace(line.charAt(left)))
			left++; // remove indentation
		if (left == line.length() // empty line
				|| line.startsWith("//", left) // comment
		)
			return;

		Matcher m = LEXEME_NAME_PATTERN.matcher(line); // find the lexeme identifier

		if (!m.find(left))
			throw new LexerException("syntax error: line must start with a lexeme identifier!", lineCount, left);

		String idf = line.substring(left, m.end());

		left = m.end();
		while (left < line.length() && Character.isWhitespace(line.charAt(left)))
			left++; // remove whitespaces between the lexeme idf and the regex

		if (left == line.length())
			throw new LexerException("syntax error: a lexeme must be followed by an expression!", lineCount, left);

		String regex;
		if (line.charAt(left) == CONST_STRING_CHAR) { // literal string
			left++; // remove start char
			int right = left;

			char c;
			while (right < line.length() && (c = line.charAt(right)) != CONST_STRING_CHAR) {
				right++;
				if (c == '\\' && right < line.length()) {
					right++; // escape a character
				}
			}

			if (right == line.length())
				throw new LexerException(
						"syntax error: missing closing " + CONST_STRING_CHAR + " before the end of the line!",
						lineCount, right - 1);

			regex = Pattern.quote(line.substring(left, right));

			int postRight = right + 1;
			while (postRight < line.length() && Character.isWhitespace(line.charAt(postRight)))
				postRight++; // remove whitespaces

			if (postRight != line.length())
				throw new LexerException("syntax error: text after the end of a literal expression!", lineCount,
						postRight);
		} else { // regex
			int right = line.length();

			while (Character.isWhitespace(line.charAt(right - 1))) // no need to check if left < right because
																	// it
																	// would have triggered an syntax error
				right--; // remove indentation after regex/literal
			regex = line.substring(left, right);
		}

		// register the lexeme
		registerLexeme(builder.build(idf, regex));
	}

	public T registerLexeme(T lexeme) {
		lexemesMap.put(lexeme.getName(), lexeme);
		return lexeme;
	}

	/**
	 * Find the next lexeme of a partial, will flush previous use and spaces
	 * 
	 * @param partial the partial read state
	 * @return the find or null if nothing was matching
	 */
	public IPartialLexemeFind<T> nextLexeme(PartialRead partial) {
		return lexemesMap.values().stream().map(lexeme -> {
			Matcher m = lexeme.getPattern().matcher(partial.getText());
			if (!m.find(partial.getRight()))
				return null;

			return new PartialLexemeFind(m.end(), lexeme, m);
		}).filter(Objects::nonNull).sorted().findFirst().orElse(null);
	}

	/**
	 * create a stream from a partial with this lexer
	 * 
	 * @param partial the partial to use
	 * @return the stream
	 */
	public ILexemeStream<T> createStream(PartialRead partial) {
		return new LexemeStream<>(this, partial);
	}

	/**
	 * create a stream from a text with this lexer
	 * 
	 * @param partial the text to use
	 * @return the stream
	 */
	public ILexemeStream<T> createStream(String text) {
		return new LexemeStream<>(this, new PartialRead(text));
	}
}
