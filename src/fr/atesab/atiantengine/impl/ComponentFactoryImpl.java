package fr.atesab.atiantengine.impl;

import fr.atesab.atiantengine.api.IComponent;
import fr.atesab.atiantengine.api.ComponentFactory;
import fr.atesab.atiantengine.api.function.FunctionnalComponent;
import fr.atesab.atiantengine.api.function.IComponentFunction;

public class ComponentFactoryImpl extends ComponentFactory<String> {

    @Override
    public IComponent<String> createText(String txt) {
        return new TextComponent(txt);
    }

    @Override
    @SuppressWarnings("unchecked")
    public IComponent<String> createTranslation(String txt, IComponent<String>[] args) {
        return concatComponents(
                new IComponent[] { createText(txt), createText(" ("), concatComponents(args), createText(")") });
    }

    @Override
    public IComponent<String> createFunctionnal(IComponentFunction<String> function, IComponent<String>[] args) {
        return new FunctionnalComponent<>(this, function, args);
    }

    @Override
    public IComponent<String> concatComponents(IComponent<String>[] components) {
        return new ConcatenateComponent(components);
    }
}
