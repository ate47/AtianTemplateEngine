package fr.atesab.atiantengine.api.lexer;

@FunctionalInterface
public interface ILexemeBuilder<T extends ILexeme> {
	T build(String name, String regex);
}
