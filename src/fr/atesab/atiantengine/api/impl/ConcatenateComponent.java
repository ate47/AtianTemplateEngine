package fr.atesab.atiantengine.api.impl;

import fr.atesab.atiantengine.api.IComponent;

public class ConcatenateComponent implements IComponent {
    private IComponent[] components;

    public ConcatenateComponent(IComponent[] components) {
        this.components = components;
    }

    @Override
    public String generate() {
        StringBuffer buffer = new StringBuffer();
        for (IComponent c : components) {
            buffer.append(c.generate());
        }
        return buffer.toString();
    }
}
