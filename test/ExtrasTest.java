import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.Util.*;
import scheduler.dummyObjects.DummyMessage;
import scheduler.queue.AbstractQueueManager;

public class ExtrasTest {

    @Test
    public void cancelationTest() {

	try {

	    ResourceScheduler scheduler = new ResourceScheduler();
	    AbstractQueueManager queue = scheduler.getQueue();

	    scheduler.forwardMessage(new DummyMessage(1, 2));
	    scheduler.forwardMessage(new DummyMessage(2, 1));
	    scheduler.forwardMessage(new DummyMessage(3, 2));

	    scheduler.cancelGroupId(2);

	    scheduler.forwardMessage(new DummyMessage(4, 3));
	    scheduler.forwardMessage(new DummyMessage(5, 2, true));
	    scheduler.forwardMessage(new DummyMessage(6, 1, true));
	    scheduler.forwardMessage(new DummyMessage(7, 3));
	    scheduler.forwardMessage(new DummyMessage(8, 3, true));

	    // We wait a bit to Messages to be enqueued
	    try {
		Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
	    } catch (Exception e) {

	    }

	    scheduler.exit();

	    scheduler.getGatewayManager().showMessageProcessingOrder();

	    // And check the final queue configuration
	    assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 3);
	    assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(), queue.getMessageCount() == 0);
	    assertTrue("Expected 7 messages processed found scheduler.getProcessedMessages()-->" + scheduler.getProcessedMessages(), scheduler.getProcessedMessages() == 7);

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void terminationTest() {
	try {

	    ResourceScheduler scheduler = new ResourceScheduler();
	    AbstractQueueManager queue = scheduler.getQueue();

	    scheduler.forwardMessage(new DummyMessage(1, 2));
	    scheduler.forwardMessage(new DummyMessage(2, 1));
	    scheduler.forwardMessage(new DummyMessage(3, 2, true));
	    scheduler.forwardMessage(new DummyMessage(4, 3));

	    try {
		scheduler.forwardMessage(new DummyMessage(5, 2, false)); // <--
									 // Termination
									 // Exception
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	    scheduler.forwardMessage(new DummyMessage(6, 1, true));
	    scheduler.forwardMessage(new DummyMessage(7, 3, true));

	    try {
		scheduler.forwardMessage(new DummyMessage(8, 3, false));// <--
									// Termination
									// Exception
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	    // We wait a bit to Messages to be enqueued
	    try {
		Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
	    } catch (Exception e) {

	    }

	    scheduler.exit();

	    scheduler.getGatewayManager().showMessageProcessingOrder();

	    // And check the final queue configuration
	    assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 3);
	    assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(), queue.getMessageCount() == 0);
	    assertTrue("Expected 8 messages processed found scheduler.getProcessedMessages()-->" + scheduler.getProcessedMessages(), scheduler.getProcessedMessages() == 8);

	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }

    @Test
    public void alternativeMessagePriorizationTest() {

	try {
	    ResourceScheduler scheduler = new ResourceScheduler("scheduler.queue.EvenGroupIdFirstQueueManager");
	    AbstractQueueManager queue = scheduler.getQueue();

	    scheduler.forwardMessage(new DummyMessage(1, 1));
	    scheduler.forwardMessage(new DummyMessage(2, 2));
	    scheduler.forwardMessage(new DummyMessage(3, 1, true));
	    scheduler.forwardMessage(new DummyMessage(4, 2, true));
	    scheduler.forwardMessage(new DummyMessage(5, 4));
	    scheduler.forwardMessage(new DummyMessage(6, 6, true));
	    scheduler.forwardMessage(new DummyMessage(7, 3, true));
	    scheduler.forwardMessage(new DummyMessage(8, 5));
	    scheduler.forwardMessage(new DummyMessage(9, 5));
	    scheduler.forwardMessage(new DummyMessage(10, 5, true));
	    scheduler.forwardMessage(new DummyMessage(11, 4));
	    scheduler.forwardMessage(new DummyMessage(12, 4, true));

	    // We wait a bit to Messages to be enqueued
	    try {
		Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
	    } catch (Exception e) {
	    }

	    scheduler.exit();

	    // And check the final queue configuration
	    assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 6);
	    assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(), queue.getMessageCount() == 0);
	    assertTrue("Expected 12 messages processed found scheduler.getProcessedMessages()-->" + scheduler.getProcessedMessages(), scheduler.getProcessedMessages() == 12);

	    scheduler.getGatewayManager().showMessageProcessingOrder();

	    List<ProcessingOrder> list = scheduler.getGatewayManager().getMessageProcessingOrder();

	    // We search first the last even groupId
	    // After that if we found any new even groupId the test is
	    // errorneous

	    boolean checkEvenFromHere = false;

	    for (int i = 0; i < list.size(); i++) {

		// In this serie we start the check after the second element
		if (i > 2) {
		    int groupId = list.get(i).getMsg().getGroupId();
		    if (!checkEvenFromHere) {
			if (groupId % 2 == 1) {
			    checkEvenFromHere = true;
			}
		    } else if (groupId % 2 == 0) {
			assertTrue("Error not even groups ids expected. Found-->" + list.get(i).getMsg(), false);
		    }
		}
	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }

}