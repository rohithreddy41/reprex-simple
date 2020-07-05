package beans;

public interface MessageManager {
    void addMessage(String message);
    void receive() throws Exception;
}
