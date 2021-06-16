package fr.atesab.atiantengine.api;

public interface IComponent {
    static String asString(IComponent... components) {
        StringBuffer buffer = new StringBuffer();

        for (IComponent iComponent : components) {
            buffer.append(iComponent.generate());
        }

        return buffer.toString();
    }

    String generate();

    default int genAsInt() {
        return (int) genAsFloat();
    }

    default float genAsFloat() {
        try {
            return Float.valueOf(generate().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
