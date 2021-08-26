package fr.atesab.atiantengine.api.function;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.ComponentFactory;

@FunctionalInterface
public interface IComponentFunction<C> {
    IComponent<C> apply(ComponentFactory<C> cFactory, IComponent<C>[] args);
}
