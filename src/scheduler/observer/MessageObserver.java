package scheduler.observer;

import scheduler.dummyObjects.DummyMessage;

public interface MessageObserver {

    public void notifyObserver(DummyMessage msg);

}
