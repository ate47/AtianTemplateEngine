package fr.atesab.atiantengine.api;

import fr.atesab.atiantengine.api.function.FunctionnalComponent;
import fr.atesab.atiantengine.api.function.IComponentFunction;

public abstract class ComponentFactory<C> {

    public abstract IComponent<C> createText(String txt);

    public abstract IComponent<C> createTranslation(String txt, IComponent<C>[] args);

    @SuppressWarnings("unchecked")
    public IComponent<C> createTranslation(String txt) {
        return createTranslation(txt, new IComponent[0]);
    }

    public IComponent<C> createFunctionnal(IComponentFunction<C> function, IComponent<C>[] args) {
        return new FunctionnalComponent<>(this, function, args);
    }

    public abstract IComponent<C> concatComponents(IComponent<C>[] components);
}
