package scheduler.gateway;

import java.util.concurrent.*;

import scheduler.Util.*;
import scheduler.dummyObjects.DummyMessage;
import scheduler.externalInterfaces.*;

public class DummyGateway implements Gateway, Runnable {

    private int id = 0;

    private volatile DummyMessage lastMsg = null;

    private volatile boolean exit = false;

    private GatewayManager gatewayManager = null;

    private final BlockingQueue sendingChannel = new ArrayBlockingQueue(1);
    

    public DummyGateway(int id, GatewayManager gatewayManager) {
	Utilities.log.debug(getClass().getName() + ":: DummyGateway(" + id + ")");
	this.id = id;
	this.gatewayManager = gatewayManager;
    }

    public int getId() {
	return id;
    }

    public BlockingQueue getChannel() {
	return sendingChannel;
    }

    public DummyMessage getLastMessageSent() {
	return lastMsg;
    }

    @Override
    public void send(Message msg) {
	try {
	    Utilities.log.debug(getClass().getName() + ":: " + toString() + "-->send(" + msg + ")");
	    gatewayManager.messageProcessingOrder.add(new ProcessingOrder(getId(), (DummyMessage) msg));
	    this.lastMsg = (DummyMessage) msg;
	    Utilities.log.debug(getClass().getName() + ":: Gateway " + toString() + " Processing message-->" + msg);
	    try {
		Thread.sleep(Utilities.PROCESSING_DELAY);
	    } catch (Exception e) {
	    }
	    Utilities.log.debug(getClass().getName() + ":: Gateway " + toString() + " Processed message-->" + msg);
	    sendingChannel.clear();// We liberate the channel
	    msg.completed();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    public boolean isAvailable() {
	//Utilities.log.debug(getClass().getName() + ":: " + toString() + ".isAvailable() --> lastMsg==" + lastMsg);
	if (lastMsg == null && sendingChannel.isEmpty()) {
	    return true;
	} else {
	    return false;
	}
    }

    public void releaseLastMessage() {
	try {
	    // We release the last message and make the resource available again
	    lastMsg = null;
	} catch (Exception e) {
	}
    }

    public void exit() {
	exit = true;
    }

    @Override
    public void run() {
	Utilities.log.debug(getClass().getName() + ":: " + toString() + " started...");
	while (!exit) {
	    try {
		if (!sendingChannel.isEmpty()) {
		    Utilities.log.debug(getClass().getName() + ":: sendingChannel.remainingCapacity()-->" + sendingChannel.remainingCapacity());
		    DummyMessage msg = (DummyMessage) sendingChannel.peek();
		    send(msg);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	Utilities.log.debug(getClass().getName() + ":: " + toString() + " exiting...");
    }

    @Override
    public String toString() {
	return "Gateway [" + id + "]";
    }

}