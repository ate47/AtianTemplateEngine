import java.io.FileInputStream;
import java.io.InputStream;

import fr.atesab.atiantengine.AtianTemplateEngine;
import fr.atesab.atiantengine.LogPolicy;
import fr.atesab.atiantengine.api.IComponent;

public class Start {
    public static void main(String[] args) throws Exception {
        AtianTemplateEngine engine = new AtianTemplateEngine();
        engine.setLogPolicy(LogPolicy.DEBUG);
        try (InputStream is = new FileInputStream("test_template.ate")) {
            IComponent component = engine.computeTemplate(is);
            String parsedText = component.generate();
            System.out.println(parsedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
