package fr.atesab.atiantengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import fr.atesab.atiantengine.api.lexer.ILexeme;
import fr.atesab.atiantengine.api.lexer.IPartialLexemeFind;

public class Lexer<T extends ILexeme> {
	private final class PartialLexemeFind implements Comparable<Lexer<T>.PartialLexemeFind>, IPartialLexemeFind<T> {
		int right;
		T lexeme;

		PartialLexemeFind(int right, T lexeme) {
			this.right = right;
			this.lexeme = lexeme;
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
		public int compareTo(Lexer<T>.PartialLexemeFind o) {
			return right - o.right;
		}

	}

	private List<T> lexemes = new ArrayList<>();

	public T registerLexeme(T lexeme) {
		this.lexemes.add(lexeme);
		return lexeme;
	}

	public IPartialLexemeFind<T> nextLexeme(PartialRead partial) {
		return lexemes.stream().map(lexeme -> {
			Matcher m = lexeme.getPattern().matcher(partial.text).region(partial.right, partial.text.length());
			if (!m.find())
				return null;

			return new PartialLexemeFind(m.end(), lexeme);
		}).filter(Objects::nonNull).sorted().findFirst().orElse(null);
	}
}
