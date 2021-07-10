package fr.atesab.atiantengine.impl.lexer;

import fr.atesab.atiantengine.Lexer;
import fr.atesab.atiantengine.PartialRead;
import fr.atesab.atiantengine.api.lexer.ILexeme;
import fr.atesab.atiantengine.api.lexer.ILexemeStream;
import fr.atesab.atiantengine.api.lexer.IPartialLexemeFind;

public class LexemeStream<L extends ILexeme> implements ILexemeStream<L> {
	private Lexer<L> lexer;
	private PartialRead partialRead;
	private IPartialLexemeFind<L> next;
	private boolean nextConsumed = true;
	private boolean syntaxError = false;

	public LexemeStream(Lexer<L> lexer, PartialRead partialRead) {
		this.lexer = lexer;
		this.partialRead = partialRead;
	}

	@Override
	public boolean hasNext() {
		if (nextConsumed) {
			nextConsumed = false;
			while ((next = lexer.nextLexeme(partialRead)) == null && partialRead.canRead(1)) {
				char c = partialRead.getText().charAt(partialRead.getRight());
				if (Character.isWhitespace(c) || c == '\n' || c == '\r') {
					partialRead.walkAndUse(1); // pass one space
				} else {
					syntaxError = true;
					return false;
				}
			}
		}
		return next != null;
	}

	/**
	 * @return the syntaxError
	 */
	@Override
	public boolean isSyntaxError() {
		return syntaxError;
	}

	@Override
	public IPartialLexemeFind<L> next() {
		hasNext();
		nextConsumed = true;
		return next;
	}
}
