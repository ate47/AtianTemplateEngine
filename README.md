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
