package me.mbogo.modsorter.message;


public class MessageLogger extends MessageBufferSingleton {
    private static boolean autoNotify;

    static {
        autoNotify = true;
    }

    public static void log(String msg) {
        doWriting(Message.MessageType.LOG, msg);
    }

    public static void error(String msg) {
        doWriting(Message.MessageType.ERR, msg);
    }

    public static void bug(String msg) {
        doWriting(Message.MessageType.BUG, msg);
    }

    private static void doWriting(Message.MessageType msgType, String msg) {
        writeMessage(Message.create(msgType, msg));

        if (autoNotify)
            notifyObservers();
    }

    public static void setAutoNotify(boolean doAutoNotify) {
        autoNotify = doAutoNotify;
    }
}
