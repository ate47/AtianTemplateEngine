package fr.atesab.atiantengine.impl;

import fr.atesab.atiantengine.api.IComponent;

public class ConcatenateComponent implements IComponent<String> {
    private IComponent<String>[] components;

    public ConcatenateComponent(IComponent<String>[] components) {
        this.components = components;
    }

    @Override
    public String generate() {
        StringBuffer buffer = new StringBuffer();
        for (IComponent<String> c : components) {
            buffer.append(c.generate());
        }
        return buffer.toString();
    }
}
