package scheduler.observer;

import scheduler.dummyObjects.DummyMessage;

public interface MessageObserver {

	void notifyObserver(DummyMessage msg);

}
