package fr.atesab.atiantengine.api;

import fr.atesab.atiantengine.api.function.IComponentFunction;

public interface IComponentFactory {
    IComponent createText(String txt);

    IComponent createTranslation(String txt, IComponent... args);

    IComponent createFunctionnal(IComponentFunction function, IComponent[] args);

    IComponent concatComponents(IComponent... components);
}
