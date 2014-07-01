package message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class MessageBuffer extends Observable
{
  private List<Message> messages = new LinkedList<Message>();

  static
  {

  }
  
  public List<Message> getMessages()
  {
    return messages;
  }
  
  public List<Message> getMessagesCopy()
  {
    List<Message> copy = new LinkedList<Message>();
    Collections.copy(copy, messages);
    return copy;
  }

  /**
   * Write a message to the message buffer.
   * @param message the message to write to the message buffer.
   */
  public void writeMessage(Message message)
  {
    messages.add(message);
    setChanged();
  }

  /**
   * Notify observers with the message buffer.  Clears the message buffer if 
   * observers were notified.
   */
  @Override
  public void notifyObservers()
  {
    boolean changed = hasChanged();

    super.notifyObservers((Object)messages);

    if (changed)
      messages.clear();
  }

  /**
   * If arg is a Message, calls writeMessage with arg as the parameter. In
   * either case, calls notifyObservers().
   */
  @Override
  public void notifyObservers(Object arg)
  {
    if (arg instanceof Message)
      writeMessage((Message)arg);

    this.notifyObservers();
  }
}
