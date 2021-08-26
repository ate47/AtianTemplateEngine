package fr.atesab.atiantengine;

import java.util.List;

import fr.atesab.atiantengine.api.IComponent;

class IdfParams<C> {
    String identifier;
    List<IComponent<C>> args;

    IdfParams(String identifier, List<IComponent<C>> args) {
        this.identifier = identifier;
        this.args = args;
    }

    @SuppressWarnings("unchecked")
    IComponent<C>[] argsAsArray() {
        return args.toArray(IComponent[]::new);
    }
}
