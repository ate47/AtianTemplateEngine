package fr.atesab.atiantengine.impl.lexer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import fr.atesab.atiantengine.api.lexer.ILexeme;

public class Lexeme implements ILexeme {
	private static final AtomicInteger ID_COUNTER = new AtomicInteger();
	private final Pattern pattern;
	private final String name;
	private final int id;

	public Lexeme(String name, String regex) {
		this(name, Pattern.compile(regex));
	}

	public Lexeme(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
		this.id = ID_COUNTER.incrementAndGet();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}

}
