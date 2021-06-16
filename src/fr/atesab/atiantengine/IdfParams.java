package fr.atesab.atiantengine;

import java.util.List;

import fr.atesab.atiantengine.api.IComponent;

class IdfParams {
    String identifier;
    List<IComponent> args;

    IdfParams(String identifier, List<IComponent> args) {
        this.identifier = identifier;
        this.args = args;
    }

    IComponent[] argsAsArray() {
        return args.toArray(IComponent[]::new);
    }
}
