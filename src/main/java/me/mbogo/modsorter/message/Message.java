package me.mbogo.modsorter.message;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Message {
    public static Message create(final MessageType messageType, final String message) {
        return new AutoValue_Message(messageType, message);
    }

    public abstract MessageType messageType();

    public abstract String message();

    public enum MessageType {
        LOG, ERR, BUG
    }
}
