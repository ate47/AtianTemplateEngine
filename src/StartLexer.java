
import java.io.FileInputStream;
import java.io.InputStream;

import fr.atesab.atiantengine.Lexer;
import fr.atesab.atiantengine.api.lexer.ILexemeStream;
import fr.atesab.atiantengine.api.lexer.IPartialLexemeFind;
import fr.atesab.atiantengine.utils.TextUtils;

public class StartLexer {

	public static void main(String[] args) throws Exception {
		Lexer<?> lexer = Lexer.buildFromFile("test_lexer.alex");
		ILexemeStream<?> stream;
		try (InputStream is = new FileInputStream("test_lexer_source.ademo")) {
			stream = lexer.createStream(TextUtils.readInput(is));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (stream.hasNext()) {
			IPartialLexemeFind<?> next = stream.next();
			System.out.println(next.getLexeme().getName() + " - " + next.getMatcher().group());
		}
		if (stream.isSyntaxError())
			System.out.println("Syntax error");
	}
}
