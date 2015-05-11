package scheduler.queue;

import java.util.*;
import java.util.concurrent.*;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.*;

public abstract class AbstractQueueManager implements Runnable {

    private volatile boolean exit = false;

    private final BlockingQueue channelFromProducer = new ArrayBlockingQueue(1);

    protected final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    protected GatewayManager gatewayManager = null;

    private boolean isMessageProcessingEnabled = true;

    public AbstractQueueManager(GatewayManager gatewayManager) {
	super();
	Utilities.log.debug(getClass().getName() + ":: QueueManager(...)");
	this.gatewayManager = gatewayManager;
    }

    public void setMessageProcessingEnabled(boolean value) {
	isMessageProcessingEnabled = value;
    }

    public boolean isMessageProcessingEnabled() {
	return isMessageProcessingEnabled;
    }

    public BlockingQueue getChannelFromProducer() {
	return channelFromProducer;
    }

    public void exit() {
	exit = true;
    }

    @Override
    public void run() {
	Utilities.log.debug(getClass().getName() + ":: Started QueueManager...");
	if (channelFromProducer == null) {
	    Utilities.log.error(getClass().getName() + ":: Error null channelProducerQueue...");
	} else {
	    Utilities.log.debug(getClass().getName() + ":: Waiting fom a Message...");
	    while (!exit) {
		try {
		    if (!channelFromProducer.isEmpty()) {
			DummyMessage msg = (DummyMessage) channelFromProducer.take();
			Utilities.log.debug(getClass().getName() + ":: Taking Message-->" + msg + " from channelFromProducer");

			try {
			    GroupIdQueue group = searchGroupId(msg.getGroupId());
			    if (group != null) {
				enqueueMsg(msg);
			    } else {
				Utilities.log.debug(getClass().getName() + ":: Trying to add Message-->" + msg + " to channelToGateway");
				if (!isMessageProcessingEnabled()) {
				    // We enqueue all messages to test
				    enqueueMsg(msg);
				} else {
				    DummyGateway gateway = gatewayManager.getAvailableGatewayForGroupId(msg.getGroupId());
				    if (gateway != null) {
					if (gateway.getChannel().offer(msg)) {
					    forwardMessage(msg);
					} else {
					    enqueueMsg(msg);
					}
				    } else {
					enqueueMsg(msg);
				    }
				}
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
			showCurrentQueueStatus();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

	    }
	}
	// Utilities.log.debug(getClass().getName() +
	// ":: Exiting QueueManager...");
    }

    protected GroupIdQueue searchGroupId(int groupId) {
	// Utilities.log.debug(getClass().getName() + ":: searchGroupId(" +
	// groupId + ")");
	Iterator iterator = queue.iterator();
	while (iterator.hasNext()) {
	    GroupIdQueue group = (GroupIdQueue) iterator.next();
	    if (group.getId() == groupId) {
		return group;
	    }
	}
	return null;
    }

    private void forwardMessage(DummyMessage msg) {
	Utilities.log.debug(getClass().getName() + "::!!! forwardMessage(" + msg + ")");
	if (msg == null) {
	    Utilities.log.error(getClass().getName() + ":: Error null null Message...");
	} else {
	    addGroupForMessage(msg);
	}
    }

    private void enqueueMsg(DummyMessage msg) {
	Utilities.log.debug(getClass().getName() + "::!!! enqueueMsg(" + msg + ")");
	if (msg == null) {
	    Utilities.log.error(getClass().getName() + ":: Error null null Message...");
	} else {
	    GroupIdQueue group = addGroupForMessage(msg);
	    group.addMessage(msg);
	}
    }

    private GroupIdQueue addGroupForMessage(DummyMessage msg) {
	GroupIdQueue group = searchGroupId(msg.getGroupId());
	if (group == null) {
	    group = new GroupIdQueue(msg.getGroupId());
	    queue.add(group);
	}
	return group;
    }

    public int getListCount() {
	return queue.size();
    }

    public int getMessageCount() {
	Utilities.log.debug(getClass().getName() + ":: getMessageCount()");
	if (queue.isEmpty()) {
	    return 0;
	} else {
	    int counter = 0;
	    Iterator iterator = queue.iterator();
	    while (iterator.hasNext()) {
		counter += ((GroupIdQueue) iterator.next()).getMessagesCount();
	    }
	    return counter;
	}
    }

    public void showCurrentQueueStatus() {
	StringBuffer aux = new StringBuffer(getClass().getName() + ":: showCurrentQueueStatus()" + System.getProperty("line.separator") + "---------------------------------------------");
	if (queue.isEmpty()) {
	    aux.append(":: Queue is Empty !!!" + System.getProperty("line.separator"));
	} else {
	    aux.append("::Queue Size-->" + queue.size() + System.getProperty("line.separator"));
	    aux.append("---------------------------------------------" + System.getProperty("line.separator"));
	    Iterator<GroupIdQueue> iterator = queue.iterator();
	    while (iterator.hasNext()) {
		aux.append(iterator.next().toString() + System.getProperty("line.separator"));
	    }
	}
	aux.append("---------------------------------------------" + System.getProperty("line.separator"));
	Utilities.log.debug(aux.toString());
    }

    /**********************************/

    // Method of the Cancellation Feature
    public int removeAllGroupIdMessagesFromQueue(int groupId) {
	Utilities.log.debug(getClass().getName() + ":: removeAllGroupIdMessagesFromQueue(" + groupId + ")");
	int total = 0;
	GroupIdQueue group = searchGroupId(groupId);
	if (group != null) {
	    total = group.getMessagesCount();
	    group.getMessagesCount();
	    group.clearMessages();
	} else {
	    Utilities.log.debug(getClass().getName() + ":: Not found queue for groupId-->" + groupId);
	    total = 0;
	}
	return total;
    }

    /**********************************/

    // Method Termination Feature
    public boolean isTerminated(int groupId) {
	Utilities.log.debug(getClass().getName() + ":: isTerminated(" + groupId + ")");
	GroupIdQueue group = searchGroupId(groupId);
	if (group != null) {
	    return group.isGroupIdTerminated();
	} else {
	    Utilities.log.debug(getClass().getName() + ":: Not found queue for groupId-->" + groupId);
	    return false;
	}
    }

    // Method Termination Feature
    public boolean terminateGroupId(int groupId) {
	Utilities.log.debug(getClass().getName() + ":: terminateGroupId(" + groupId + ")");
	GroupIdQueue group = searchGroupId(groupId);
	if (group != null) {
	    group.terminateGroupId();
	    return true;
	} else {
	    Utilities.log.debug(getClass().getName() + ":: Not found queue for groupId-->" + groupId);
	    return false;
	}
    }

    protected List<GroupIdQueue> geNotEmptyLists() {
	Utilities.log.debug(getClass().getName() + ":: getNotEmptyLists()");
	List<GroupIdQueue> list = new ArrayList<GroupIdQueue>();
	if (!queue.isEmpty()) {
	    Iterator iterator = queue.iterator();
	    while (iterator.hasNext()) {
		GroupIdQueue group = ((GroupIdQueue) iterator.next());
		if (group.getMessagesCount() > 0) {
		    list.add(group);
		}
	    }
	}
	return list;
    }

    /**********************************/

    public abstract DummyMessage getAternativeMsgForGateway();

    public abstract DummyMessage extractMessage();

    public abstract DummyMessage extractNextMessageForGroupId(int groupId);
}