package fr.atesab.atiantengine.api.function;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.ComponentFactory;

public abstract class ComponentFunction<C> implements IComponentFunction<C> {

    @FunctionalInterface
    public interface ComponentFunction0<C> {
        IComponent<C> apply(ComponentFactory<C> cFactory);
    }

    @FunctionalInterface
    public interface ComponentFunction1<C> {
        IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C> arg1);
    }

    @FunctionalInterface
    public interface ComponentFunction2<C> {
        IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C> arg1, IComponent<C> arg2);
    }

    @FunctionalInterface
    public interface ComponentFunction3<C> {
        IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C> arg1, IComponent<C> arg2, IComponent<C> arg3);
    }

    @FunctionalInterface
    public interface ComponentFunction4<C> {
        IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C> arg1, IComponent<C> arg2, IComponent<C> arg3,
                IComponent<C> arg4);
    }

    /**
     * Create static text field value
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param text     the text
     * @return the component function
     */
    public static <C> ComponentFunction<C> constField(ComponentFactory<C> cFactory, String text) {
        return func0(cFactory, f -> f.createText(text));
    }

    /**
     * Create function without argument
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param function the function
     * @return the component function
     */
    public static <C> ComponentFunction<C> func0(ComponentFactory<C> cFactory, ComponentFunction0<C> function) {
        return new ComponentFunction<C>(0) {
            @Override
            public IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args) {
                return function.apply(cFactory);
            }
        };
    }

    /**
     * Create function with 1 argument
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param function the function
     * @return the component function
     */
    public static <C> ComponentFunction<C> func1(ComponentFunction1<C> function) {
        return new ComponentFunction<C>(1) {
            @Override
            public IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args) {
                return function.apply(cFactory, args[0]);
            }
        };
    }

    /**
     * Create function with 2 arguments
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param function the function
     * @return the component function
     */
    public static <C> ComponentFunction<C> func2(ComponentFunction2<C> function) {
        return new ComponentFunction<C>(2) {
            @Override
            public IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args) {
                return function.apply(cFactory, args[0], args[1]);
            }
        };
    }

    /**
     * Create function with 3 arguments
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param function the function
     * @return the component function
     */
    public static <C> ComponentFunction<C> func3(ComponentFunction3<C> function) {
        return new ComponentFunction<C>(3) {
            @Override
            public IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args) {
                return function.apply(cFactory, args[0], args[1], args[2]);
            }
        };
    }

    /**
     * Create function with 4 arguments
     * 
     * @param <C>      the component type
     * @param cFactory the factory to build the function
     * @param function the function
     * @return the component function
     */
    public static <C> ComponentFunction<C> func4(ComponentFunction4<C> function) {
        return new ComponentFunction<C>(4) {
            @Override
            public IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args) {
                return function.apply(cFactory, args[0], args[1], args[2], args[3]);
            }
        };
    }

    private int count;

    public ComponentFunction(int count) {
        this.count = count;
    }

    public int argumentCount() {
        return count;
    }

    @Override
    public final IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C>[] args) {
        if (args.length != count)
            return cFactory.createText("bad_arg_count " + args.length + " != " + count + "!");
        return apply0(cFactory, args);
    }

    protected abstract IComponent<C> apply0(ComponentFactory<C> cFactory, IComponent<C>[] args);
}
