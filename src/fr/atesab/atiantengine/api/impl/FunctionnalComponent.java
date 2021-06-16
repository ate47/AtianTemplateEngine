package fr.atesab.atiantengine.api.impl;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.function.IComponentFunction;

public class FunctionnalComponent implements IComponent {
    private IComponentFunction function;
    private IComponent[] args;

    public FunctionnalComponent(IComponentFunction function, IComponent[] args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public String generate() {
        return function.apply(args);
    }

}
