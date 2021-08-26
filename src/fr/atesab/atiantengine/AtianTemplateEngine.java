package fr.atesab.atiantengine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.ComponentFactory;
import fr.atesab.atiantengine.api.exception.EngineException;
import fr.atesab.atiantengine.api.function.ComponentFunction;
import fr.atesab.atiantengine.api.function.IComponentFunction;
import fr.atesab.atiantengine.impl.ComponentFactoryImpl;
import fr.atesab.atiantengine.utils.TextUtils;

public class AtianTemplateEngine<C> {
    public static final String ENGINE_VERSION = "0.2.0";
    public static final int ENGINE_VERSION_IDENTIFIER = 2;
    public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z_.][0-9A-Za-z_.]*");

    public static <C> AtianTemplateEngine<C> of(ComponentFactory<C> factory) {
        return new AtianTemplateEngine<>(factory);
    }

    public static AtianTemplateEngine<String> stringEngine() {
        return new AtianTemplateEngine<>(new ComponentFactoryImpl());
    }

    @SuppressWarnings("unchecked")
    private IComponent<C>[] array(IComponent<?>... elements) {
        return (IComponent<C>[]) elements;
    }

    private Map<String, IComponentFunction<C>> functions = new HashMap<>();
    private ComponentFactory<C> factory;
    private LogPolicy logPolicy = LogPolicy.ERROR;

    private AtianTemplateEngine(ComponentFactory<C> factory) {
        this.factory = factory;
        registerInternalFunctions();
    }

    public ComponentFactory<C> getFactory() {
        return factory;
    }

    public void setLogPolicy(LogPolicy logPolicy) {
        this.logPolicy = logPolicy;
    }

    /**
     * Compute the template from a string
     * 
     * @param text the text to parse
     * @return the parsed components
     */
    public IComponent<C> computeTemplate(String text) {
        PartialRead partial = new PartialRead(text);
        try {
            return readComponents(partial);
        } catch (EngineException e) {
            if (logPolicy == LogPolicy.DEBUG)
                e.printStackTrace();
            return factory
                    .concatComponents(array(e.getMsg(), factory.createText(" " + e.getPartialRead().getLastState())));
        } catch (Throwable e) {
            if (logPolicy == LogPolicy.DEBUG)
                e.printStackTrace();
            return factory.createTranslation("atiantengine.error.exception", array(factory.createText(e.getMessage())));
        }
    }

    /**
     * Compute the template from a inputstream
     * 
     * @param input the stream to load
     * @return the parsed components
     */
    @SuppressWarnings("unchecked")
    public IComponent<C> computeTemplate(InputStream input) {
        try {
            return computeTemplate(TextUtils.readInput(input));
        } catch (Exception e) {
            if (logPolicy == LogPolicy.DEBUG)
                e.printStackTrace();
            return factory.createTranslation("atiantengine.error.io",
                    new IComponent[] { factory.createText(e.getMessage()) });
        }
    }

    private void readComponentsUseIfNeeded(PartialRead partial, List<IComponent<C>> l) {
        String s = partial.use();
        if (!s.isEmpty()) {
            l.add(factory.createText(s));
        }
    }

    private IComponent<C> readComponents(PartialRead partial, String... ends) {
        return factory.concatComponents(array(readComponentsAsList(partial, ends).toArray(IComponent[]::new)));
    }

    private String readIdf(PartialRead partial) {
        Matcher match = partial.walkAndRead(IDENTIFIER_PATTERN);

        if (match == null) {
            throw new EngineException(partial, factory.createTranslation("atiantengine.error.syntax.missing",
                    array(factory.createTranslation("atiantengine.object.identifier"))));
        }

        return match.group();
    }

    private IdfParams<C> readIdfParams(PartialRead partial) {
        partial.useSpaces(); // remove spaces

        String idf = readIdf(partial);

        partial.useSpaces();

        List<IComponent<C>> args = new ArrayList<>();

        while (partial.next(";;")) {
            partial.walkAndUse(2); // consume ;;
            IComponent<C> a = readComponents(partial, ";;", "}}");
            args.add(a);

        }
        // here we have a }} because a syntax error/while iteration would have be
        // triggered otherwise
        partial.walkAndUse(2); // consume }}
        return new IdfParams<>(idf, args);
    }

    private List<IComponent<C>> readComponentsAsList(PartialRead partial, String... ends) {
        List<IComponent<C>> l = new ArrayList<>();

        while (partial.canRead(1)) {
            // check end of the group
            for (String end : ends) {
                if (partial.next(end)) {
                    String s = partial.use();
                    if (!s.isEmpty()) {
                        l.add(factory.createText(s));
                    }
                    return l;
                }
            }

            if (partial.next("${{")) { // FUNCTIONS
                readComponentsUseIfNeeded(partial, l);
                partial.walkAndUse(3); // remove ${{
                IdfParams<C> ip = readIdfParams(partial);
                IComponentFunction<C> function = functions.get(ip.identifier.toLowerCase());
                if (function == null) {
                    throw new EngineException(partial, factory.createTranslation("atiantengine.error.unknown",
                            array(factory.createText(ip.identifier))));
                }
                l.add(factory.createFunctionnal(function, ip.argsAsArray()));
            } else if (partial.next("#{{")) { // TRANSLATIONS
                readComponentsUseIfNeeded(partial, l);
                partial.walkAndUse(3); // remove #{{
                IdfParams<C> ip = readIdfParams(partial);
                l.add(factory.createTranslation(ip.identifier, ip.argsAsArray()));
            } else { // RAW TEXT
                partial.walk(1);
            }
        }

        if (ends.length != 0) {
            throw new EngineException(partial, factory.createTranslation("atiantengine.error.syntax.missing",
                    array(factory.createText(Arrays.toString(ends)))));
        }

        String s = partial.use();
        if (!s.isEmpty()) {
            l.add(factory.createText(s));
        }
        return l;
    }

    private void registerInternalFunctions() {
        registerFunction("engine.version", ComponentFunction.constField(factory, ENGINE_VERSION));
        registerFunction("sum", (f, args) -> {
            int a = 0;
            for (IComponent<C> c : args)
                a += c.genAsInt();
            return f.createText(String.valueOf(a));
        });
        registerFunction("sumf", (f, args) -> {
            float a = 0;
            for (IComponent<C> c : args)
                a += c.genAsFloat();
            return f.createText(String.valueOf(a));
        });
        registerFunction("mul", (f, args) -> {
            int a = 1;
            for (IComponent<C> c : args)
                a *= c.genAsInt();
            return f.createText(String.valueOf(a));
        });
        registerFunction("mulf", (f, args) -> {
            float a = 1;
            for (IComponent<C> c : args)
                a *= c.genAsFloat();
            return f.createText(String.valueOf(a));
        });
    }

    /**
     * Register a function to the template engine
     * 
     * @param name     the function identifier (Not case sensitive)
     * @param function the function to register
     * @throws NullPointerException     if name or function are null
     * @throws IllegalArgumentException if the name doesn't match the pattern
     * @see AtianTemplateEngine#IDENTIFIER_PATTERN
     */
    public void registerFunction(String name, IComponentFunction<C> function) {
        Objects.requireNonNull(name, "name can't be null");
        Objects.requireNonNull(function, "function can't be null");

        // check name pattern
        if (!IDENTIFIER_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("The name must match the regex '[A-Za-z_.][0-9A-Za-z_.]*'");
        }

        functions.put(name.toLowerCase(), function);
    }

}
