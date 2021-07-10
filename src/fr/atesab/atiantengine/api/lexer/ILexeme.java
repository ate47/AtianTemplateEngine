package fr.atesab.atiantengine.api.lexer;

import java.util.regex.Pattern;

public interface ILexeme {
	int getId();
	String getName();
	Pattern getPattern();
}
