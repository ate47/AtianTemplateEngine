package fr.atesab.atiantengine.api.lexer;

public interface IPartialLexemeFind<T extends ILexeme> {
	int getRight();

	T getLexeme();
}
