package tracking.mdb;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;


@Singleton
@ApplicationScoped
public class MDBMessageTracker {



    private int messgeReceivedCount = 0;

    public int increseMessageReceivedCount(){
        return ++messgeReceivedCount;
    }

    public int getMessgeReceivedCount(){
        return messgeReceivedCount;
    }
}
