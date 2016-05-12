package me.mbogo.modsorter.message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class MessageBuffer extends Observable {
    private List<Message> messages = new LinkedList<Message>();

    static {

    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getMessagesCopy() {
        List<Message> copy = new LinkedList<>();
        Collections.copy(copy, messages);
        return copy;
    }

    /**
     * Write a me.mbogo.modsorter.message to the me.mbogo.modsorter.message buffer.
     *
     * @param message the me.mbogo.modsorter.message to write to the me.mbogo.modsorter.message buffer.
     */
    public void writeMessage(Message message) {
        messages.add(message);
        setChanged();
    }

    /**
     * Notify observers with the me.mbogo.modsorter.message buffer.  Clears the me.mbogo.modsorter.message buffer if
     * observers were notified.
     */
    @Override
    public void notifyObservers() {
        boolean changed = hasChanged();

        super.notifyObservers(messages);

        if (changed)
            messages.clear();
    }

    /**
     * If arg is a Message, calls writeMessage with arg as the parameter. In
     * either case, calls notifyObservers().
     */
    @Override
    public void notifyObservers(Object arg) {
        if (arg instanceof Message)
            writeMessage((Message) arg);

        this.notifyObservers();
    }
}
