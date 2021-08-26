import java.io.FileInputStream;
import java.io.InputStream;

import fr.atesab.atiantengine.AtianTemplateEngine;
import fr.atesab.atiantengine.LogPolicy;

public class Start {
    public static void main(String[] args) throws Exception {
        var engine = AtianTemplateEngine.stringEngine();
        engine.setLogPolicy(LogPolicy.DEBUG);
        try (InputStream is = new FileInputStream("test_template.ate")) {
            var component = engine.computeTemplate(is);
            var parsedText = component.generate();
            System.out.println(parsedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
