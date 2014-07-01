package message;

import java.util.Observer;

class MessageBufferSingleton
{
  private static MessageBuffer messageBuffer = new MessageBuffer();
  
  static 
  {
    
  }
  
  public static void writeMessage(Message msg)
  {
    messageBuffer.writeMessage(msg);
  }
  
  public static void addObserver(Observer o)
  {
    messageBuffer.addObserver(o);
  }
  
  public static int countObservers()
  {
    return messageBuffer.countObservers();
  }
  
  public static void deleteObserver(Observer o)
  {
    messageBuffer.deleteObserver(o);
  }
  
  public static void deleteObservers()
  {
    messageBuffer.deleteObservers();
  }
  
  public static boolean hasChanged()
  {
    return messageBuffer.hasChanged();
  }
  
  public static void notifyObservers()
  {
    messageBuffer.notifyObservers();
  }
  
  public static void notifyObservers(Object arg)
  {
    messageBuffer.notifyObservers(arg);
  }
}
