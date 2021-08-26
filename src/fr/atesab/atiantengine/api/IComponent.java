package fr.atesab.atiantengine.api;

public interface IComponent<C> {
    static String asString(IComponent<String>[] components) {
        StringBuffer buffer = new StringBuffer();

        for (IComponent<String> iComponent : components) {
            buffer.append(iComponent.generate());
        }

        return buffer.toString();
    }

    C generate();

    default String generateAsString() {
        return String.valueOf(generate());
    }

    default int genAsInt() {
        return Integer.parseInt(generateAsString());
    }

    default float genAsFloat() {
        return Float.parseFloat(generateAsString());
    }

    default boolean genAsBoolean() {
        return Boolean.parseBoolean(generateAsString());
    }
}
