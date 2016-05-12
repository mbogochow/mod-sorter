package me.mbogo.modsorter.message;

public class MessageLogger extends MessageBufferSingleton {
    private static boolean autoNotify;

    static {
        autoNotify = true;
    }

    public static void log(final String msg) {
        doWriting(Message.MessageType.LOG, msg);
    }

    public static void error(final String msg) {
        doWriting(Message.MessageType.ERR, msg);
    }

    public static void bug(final String msg) {
        doWriting(Message.MessageType.BUG, msg);
    }

    private static void doWriting(final Message.MessageType msgType, final String msg) {
        writeMessage(Message.create(msgType, msg));

        if (autoNotify)
            notifyObservers();
    }

    public static void setAutoNotify(final boolean doAutoNotify) {
        autoNotify = doAutoNotify;
    }
}
