package fr.atesab.atiantengine.impl;

import fr.atesab.atiantengine.api.IComponent;

public class TextComponent implements IComponent {
    private String text;

    public TextComponent(String text) {
        this.text = text;
    }

    @Override
    public String generate() {
        return text;
    }
}
