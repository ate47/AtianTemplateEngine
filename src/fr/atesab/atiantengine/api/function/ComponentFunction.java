package fr.atesab.atiantengine.api.function;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import fr.atesab.atiantengine.api.IComponent;

public abstract class ComponentFunction implements IComponentFunction {

    public static ComponentFunction constField(String text) {
        return func0(() -> text);
    }

    public static ComponentFunction func0(Supplier<String> supplier) {
        return new ComponentFunction(0) {
            @Override
            public String apply0(IComponent[] args) {
                return supplier.get();
            }
        };
    }

    public static ComponentFunction func1(Function<IComponent, String> function) {
        return new ComponentFunction(1) {
            @Override
            public String apply0(IComponent[] args) {
                return function.apply(args[0]);
            }
        };
    }

    public static ComponentFunction func2(BiFunction<IComponent, IComponent, String> function) {
        return new ComponentFunction(2) {
            @Override
            public String apply0(IComponent[] args) {
                return function.apply(args[0], args[1]);
            }
        };
    }

    private int count;

    private ComponentFunction(int count) {
        this.count = count;
    }

    public int argumentCount() {
        return count;
    }

    @Override
    public final String apply(IComponent[] args) {
        if (args.length != count)
            return "bad_arg_count " + args.length + " != " + count + "!";
        return apply0(args);
    }

    protected abstract String apply0(IComponent[] args);
}
