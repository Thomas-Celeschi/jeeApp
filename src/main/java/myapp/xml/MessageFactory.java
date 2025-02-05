package myapp.xml;

import lombok.Data;

@Data
public class MessageFactory {

    private String messageBase = "";

    Counter counter;

    public String newMessage() {
        return (messageBase + (counter.nextValue()));
    }

}
