package fr.atesab.atiantengine.api.lexer;

import java.util.regex.Matcher;

public interface IPartialLexemeFind<T extends ILexeme> {
	int getRight();

	T getLexeme();

	Matcher getMatcher();
}
