# Atian Template Engine <!-- omit in toc -->

Template engine in a will to be integrate in a Minecraft mod.

**Table of content**:

- [Usage](#usage)
  - [Engine](#engine)
    - [Create](#create)
  - [Functions](#functions)
    - [In template](#in-template)
    - [Register](#register)
  - [Translations](#translations)
    - [In template](#in-template-1)
  - [Lexer engine](#lexer-engine)
    - [Lexeme file format](#lexeme-file-format)
      - [Lexeme declaration](#lexeme-declaration)
      - [Comment](#comment)

# Usage

## Engine

### Create

```java
import java.io.FileInputStream;
import java.io.InputStream;

import fr.atesab.atiantengine.AtianTemplateEngine;
import fr.atesab.atiantengine.LogPolicy;
import fr.atesab.atiantengine.api.IComponent;

public class Example {
    public static void main(String[] args) throws Exception {
        AtianTemplateEngine engine = new AtianTemplateEngine();

        engine.setLogPolicy(LogPolicy.DEBUG); // to get the error in the console and not only in the rendering

        try (InputStream is = new FileInputStream("test_template.ate")) {
            IComponent component = engine.computeTemplate(is); // the component created by the template
            String parsedText = component.generate(); // generate a text from the parsed text
            System.out.println(parsedText); // show it, but you can do whatever you want
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Functions

### In template

parami is a template.

```
Calling a function/field: ${{identifier}}
Calling a function with arguments: ${{identifier;;param1;;param2}}
```

### Register

```Java
// a const field getter
registerFunction("my.field", ComponentFunction.constField("my field"));
registerFunction("my.field.dyna", ComponentFunction.func0(() -> myField));
// a simple sum function
engine.registerFunction("my.sum",
    ComponentFunction.func2((arg1, arg2) ->
        String.valueOf(arg1.genAsFloat() + arg2.genAsFloat())));
```

```
${{my.field}}
${{my.field.dyna}}
${{my.sum;;3.5;;2.5}}
```

## Translations

### In template

parami is a template.

```
Calling a translation: #{{identifier}}
Calling a translation with arguments: #{{identifier;;param1;;param2}}
```


## Lexer engine

The lexer, represented by the [Lexer class](src/fr/atesab/atiantengine/Lexer.java) can create a stream of lexeme from a file, you can add each lexeme pattern using the ``registerLexeme(T lexeme)`` method or create a file and ingest it using the ``buildFromFile(String file)`` method like this:

```java

import java.io.FileInputStream;
import java.io.InputStream;

import fr.atesab.atiantengine.Lexer;
import fr.atesab.atiantengine.api.lexer.ILexemeStream;
import fr.atesab.atiantengine.api.lexer.IPartialLexemeFind;
import fr.atesab.atiantengine.utils.TextUtils;

public class Example {

	public static void main(String[] args) throws Exception {
    // create a lexer from a file

		Lexer<?> lexer = Lexer.buildFromFile("test_lexer.alex");

    // create a lexeme stream from a file
		ILexemeStream<?> stream;
		try (InputStream is = new FileInputStream("test_lexer_source.ademo")) {
			stream = lexer.createStream(TextUtils.readInput(is));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

    // show the lexemes from the stream
		while (stream.hasNext()) {
			IPartialLexemeFind<?> next = stream.next();
			System.out.println(next.getLexeme().getName() + " - " + next.getMatcher().group());
		}
		if (stream.isSyntaxError())
			System.out.println("Syntax error");
	}
}
```

### Lexeme file format

The lexeme file list lexemes

#### Lexeme declaration

A lexeme is composed of an identifier followed by a regular expression (regex) or a literal string (literal). the identifier must follow the regex ``[A-Za-z_][A-Za-z_0-9]*``, you can add escaped characters like ``\n`` ``\r`` ``\t`` ``\b`` ``\f`` ``\\``.

```java
identifier regex
identifier "literal"
```

**Example**

```
// + operator
PLUS "+"
// an integer, like 42
INTEGER [1-9][0-9]*
```

#### Comment

You can add a comment in you file by adding ``//`` before your comment, **you can't put a comment after a lexeme declaration**

```java
// my comment
```

