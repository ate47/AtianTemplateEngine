package fr.atesab.atiantengine.api.lexer;

public interface ILexemeStream<L extends ILexeme> {
	/**
	 * @return the next partial find, or null if {@link #hasNext()} == false
	 */
	IPartialLexemeFind<L> next();

	/**
	 * try to find a next lexeme, wouldn't consume the lexeme until {@link #next()}
	 * is called, if the result is false, call {@link #isSyntaxError()} to see if
	 * the end is due to a syntax error
	 * 
	 * @return if a next lexeme can be find
	 * @see #next()
	 * @see #isSyntaxError()
	 */
	boolean hasNext();

	/**
	 * @return if the {@link #hasNext()} == false is due to a syntax error
	 */
	boolean isSyntaxError();
}
