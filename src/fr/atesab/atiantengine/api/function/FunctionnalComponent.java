package fr.atesab.atiantengine.api.function;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.ComponentFactory;

public class FunctionnalComponent<C> implements IComponent<C> {
    private IComponentFunction<C> function;
    private IComponent<C>[] args;
    private ComponentFactory<C> factory;

    public FunctionnalComponent(ComponentFactory<C> factory, IComponentFunction<C> function, IComponent<C>[] args) {
        this.factory = factory;
        this.function = function;
        this.args = args;
    }

    @Override
    public C generate() {
        return function.apply(factory, args).generate();
    }
}
