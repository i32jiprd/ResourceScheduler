package scheduler.dummyObjects;


import scheduler.Util.Utilities;
import scheduler.externalInterfaces.Message;
import scheduler.observer.MessageObserver;

public class DummyMessage implements Message {

    private int id = 0;

    private int groupId = 0;

    private boolean isTerminationMessage = false;

    private MessageObserver observer;

    public DummyMessage(int id, int groupId) {
	this(id, groupId, false);
    }

    public DummyMessage(int id, int groupId, boolean isTerminationMessage) {
	super();
	this.id = id;
	this.groupId = groupId;
	this.isTerminationMessage = isTerminationMessage;
    }

    public void setObserver(MessageObserver observer) {
	Utilities.log.debug(getClass().getName() + "::!!! Setting Message observer...");
	this.observer = observer;
    }

    public int getId() {
	return id;
    }

    public int getGroupId() {
	return groupId;
    }

    @Override
    public void completed() {
	Utilities.log.debug(getClass().getName() + ":: completed() message-->" + toString());
	if (observer != null) {
	    observer.notifyObserver(this);
	} else {
	    Utilities.log.warn(getClass().getName() + "::  null observer in Message-->" + toString());
	}
    }

    public boolean isTerminationMessage() {
	return isTerminationMessage;
    }

    @Override
    public String toString() {
	return "[message:" + id + "] [groupId:" + getGroupId() + "]";
    }

}
