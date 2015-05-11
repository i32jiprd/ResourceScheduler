import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.Util.*;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.GatewayManager;
import scheduler.queue.AbstractQueueManager;

public class BaseTest {

    @Test
    public void baseExampleTest() {

	try{
	
	ResourceScheduler scheduler = new ResourceScheduler();
	AbstractQueueManager queue = scheduler.getQueue();

	scheduler.forwardMessage(new DummyMessage(1, 2));
	scheduler.forwardMessage(new DummyMessage(2, 1, true));
	scheduler.forwardMessage(new DummyMessage(3, 2, true));
	scheduler.forwardMessage(new DummyMessage(4, 3, true));

	// We wait a bit to Messages to be enqueued
	try {
	    Thread.sleep(Utilities.PROCESSING_DELAY*scheduler.getProcessedMessages());
	} catch (Exception e) {

	}

	scheduler.exit();

	// And check the final queue configuration
	assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 3);
	assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(), queue.getMessageCount() == 0);

	// Checking processing order
	GatewayManager gatewayManager = scheduler.getGatewayManager();
	gatewayManager.showMessageProcessingOrder();
	List list = gatewayManager.getMessageProcessingOrder();

	assertTrue("Expected 4 messages proccesed, found  -->" + list.size(), list.size() == 4);

	ProcessingOrder order = (ProcessingOrder) list.get(0);
	assertTrue("Expected Gateway 0 and Message (1,2) found -->" + order, order.getGatewayId() == 0 && order.getMsg().getId() == 1);

	order = (ProcessingOrder) list.get(1);
	assertTrue("Expected Gateway 0 and Message (3,2) found -->" + order, order.getGatewayId() == 0 && order.getMsg().getId() == 3);

	order = (ProcessingOrder) list.get(2);
	assertTrue("Expected Gateway 0 and Message (2,1) found -->" + order, order.getGatewayId() == 0 && order.getMsg().getId() == 2);

	order = (ProcessingOrder) list.get(3);
	assertTrue("Expected Gateway 0 and Message (4,3) found -->" + order, order.getGatewayId() == 0 && order.getMsg().getId() == 4);

	}catch(Exception e){
	    e.printStackTrace();
	}	
    }
}
