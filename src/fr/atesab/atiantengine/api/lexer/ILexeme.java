package fr.atesab.atiantengine.api.lexer;

import java.util.regex.Pattern;

public interface ILexeme {
	int getId();
	Pattern getPattern();
}
