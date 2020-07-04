package beans;

import javax.jms.JMSException;

public interface MessageManager {
    void addMessage(String message);
    void receive() throws JMSException;
}
