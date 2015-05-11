package scheduler.queue;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;

public class GroupIdQueue {

    private int groupId = 0;

    private final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    // Used for Extra feature "Termination Messages"
    private boolean isTerminated = false;

    public GroupIdQueue(int groupId) {
	//Utilities.log.debug(getClass().getName() + "::GroupIdQueue(" + groupId + ")");
	this.groupId = groupId;
    }

    public int getId() {
	return groupId;
    }

    public void addMessage(DummyMessage msg) {
	//Utilities.log.debug(getClass().getName() + ":: addMessage(" + msg + ")");
	if (msg != null) {
	    //TODO: a nice feature will be check to avoid duplicated Messages in list.
	    queue.add(msg);
	    //Utilities.log.debug(getClass().getName() + "::!!! added Message-->" + msg + " to queue-->" + getId());
	} else {
	    Utilities.log.error(getClass().getName() + ":: Error adding msg-->" + msg);
	}
    }

    public int getMessagesCount() {
	return queue.size();
    }

    public DummyMessage extractMessage() {
	if (!queue.isEmpty()) {
	    return (DummyMessage) queue.poll();
	} else {
	    return null;
	}
    }

    // Used for Extra feature "Cancellation"
    public void clearMessages() {
	queue.clear();
    }

    // Method used to implement Extra Credit "Termination Messages"
    public void terminateGroupId() {
	isTerminated = true;
    }

    // Method used to implement Extra Credit "Termination Messages"
    public boolean isGroupIdTerminated() {
	return isTerminated;
    }

    @Override
    public String toString() {
	String aux = "[" + getId() + "][" + isGroupIdTerminated() + "][Size:" + getMessagesCount() + "]-->";
	if (queue.isEmpty()) {
	    aux += "Empty List!!!";
	} else {
	    Iterator iterator = queue.iterator();
	    while (iterator.hasNext()) {
		aux += ((DummyMessage) iterator.next()).toString()+" | ";
	    }
	}
	return aux;
    }

}
