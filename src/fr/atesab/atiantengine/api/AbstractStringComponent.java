package fr.atesab.atiantengine.api;

public abstract class AbstractStringComponent<C> implements IComponent<C> {

    protected final String text;

    public AbstractStringComponent(String text) {
        this.text = text;
    }

    @Override
    public String generateAsString() {
        return text;
    }
}
