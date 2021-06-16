package fr.atesab.atiantengine.api.function;

import fr.atesab.atiantengine.api.IComponent;

@FunctionalInterface
public interface IComponentFunction {
    String apply(IComponent[] args);
}
