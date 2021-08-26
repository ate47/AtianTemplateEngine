package fr.atesab.atiantengine.api.exception;

import fr.atesab.atiantengine.PartialRead;
import fr.atesab.atiantengine.api.IComponent;

public class EngineException extends RuntimeException {
    private PartialRead partialRead;
    private IComponent<?> msg;

    public EngineException(PartialRead partialRead, IComponent<?> msg, Throwable throwable) {
        super(msg.generateAsString(), throwable);
        this.partialRead = partialRead;
        this.msg = msg;
    }

    public EngineException(PartialRead partialRead, IComponent<?> msg) {
        super(msg.generateAsString());
        this.partialRead = partialRead;
        this.msg = msg;
    }

    public IComponent<?> getMsg() {
        return msg;
    }

    public PartialRead getPartialRead() {
        return partialRead;
    }
}
