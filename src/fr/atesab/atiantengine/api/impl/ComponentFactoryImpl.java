package fr.atesab.atiantengine.api.impl;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.IComponentFactory;
import fr.atesab.atiantengine.api.function.IComponentFunction;

public class ComponentFactoryImpl implements IComponentFactory {

    @Override
    public IComponent createText(String txt) {
        return new TextComponent(txt);
    }

    @Override
    public IComponent createTranslation(String txt, IComponent... args) {
        return concatComponents(createText(txt), createText(" ("), concatComponents(args), createText(")"));
    }

    @Override
    public IComponent createFunctionnal(IComponentFunction function, IComponent[] args) {
        return new FunctionnalComponent(function, args);
    }
    @Override
    public IComponent concatComponents(IComponent... components) {
        return new ConcatenateComponent(components);
    }
}
