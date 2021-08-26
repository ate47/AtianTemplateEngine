package fr.atesab.atiantengine.impl;

import fr.atesab.atiantengine.api.AbstractStringComponent;

public class TextComponent extends AbstractStringComponent<String> {
    public TextComponent(String text) {
        super(text);
    }

    @Override
    public String generate() {
        return generateAsString();
    }
}
