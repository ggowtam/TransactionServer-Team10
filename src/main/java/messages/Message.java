package messages;

import java.io.Serializable;

import constants.MessageTypes;

public class Message implements Serializable, MessageTypes {

    int type;
    Object msgContent;

    public Message(int type, Object msgContent) {
        this.type = type;
        this.msgContent = msgContent;
    }

    public Message(int type) {
        this(type, null);
    }

    // getter for type
    public int getType() {
        return type;
    }

    // setter for type
    public void setType(int type) {
        this.type = type;
    }

    public Object getContent() {
        return msgContent;
    }

    public void setContent(Object msgContent) {
        this.msgContent = msgContent;
    }

}
