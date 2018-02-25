import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.Util.*;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.GatewayManager;
import scheduler.queue.AbstractQueueManager;

public class MultipleGatewayTest {

	@Test
	public void twoGatewayExampleTest() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(2);
			final AbstractQueueManager queue = scheduler.getQueue();

			scheduler.forwardMessage(new DummyMessage(1, 1));
			scheduler.forwardMessage(new DummyMessage(2, 3));
			scheduler.forwardMessage(new DummyMessage(3, 5, true));
			scheduler.forwardMessage(new DummyMessage(4, 2));
			scheduler.forwardMessage(new DummyMessage(5, 2));
			scheduler.forwardMessage(new DummyMessage(6, 1, true));
			scheduler.forwardMessage(new DummyMessage(7, 3, true));
			scheduler.forwardMessage(new DummyMessage(8, 4, true));
			scheduler.forwardMessage(new DummyMessage(9, 2, true));

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
			} catch (final Exception e) {

			}

			scheduler.exit();

			// And chech the final queue configuration
			assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 5);
			assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 0);

			// Checking processing order
			final GatewayManager gatewayManager = scheduler.getGatewayManager();
			gatewayManager.showMessageProcessingOrder();
			final List<ProcessingOrder> list = gatewayManager.getMessageProcessingOrder();

			assertTrue("Expected 9 messages proccesed, found  -->" + list.size(), list.size() == 9);

			final Hashtable<Integer, Integer> htchecks = new Hashtable<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				final ProcessingOrder order = (ProcessingOrder) list.get(i);
				final DummyMessage msg = order.getMsg();
				final int groupId = msg.getGroupId();
				final int gatewayId = order.getGatewayId();

				if (htchecks.get(groupId) == null) {
					htchecks.put(groupId, gatewayId);
				} else {
					int referencedGateway = htchecks.get(groupId);
					// We chech each insertion

					// Utilities.log.debug("Checking
					// [referencedGateway],[groupId]-->["+referencedGateway+"]["+groupId+"]-->"+order
					// );

					assertTrue(
							"Expected Gateway [" + referencedGateway + "]  for groupId--> " + groupId + "  found -->"
									+ order.getGatewayId() + " instead in order-->" + order + order,
							referencedGateway == order.getGatewayId());
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void threeGatewayExampleTest() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(3);
			final AbstractQueueManager queue = scheduler.getQueue();

			scheduler.forwardMessage(new DummyMessage(1, 1));
			scheduler.forwardMessage(new DummyMessage(2, 3));
			scheduler.forwardMessage(new DummyMessage(3, 5, true));
			scheduler.forwardMessage(new DummyMessage(4, 2));
			scheduler.forwardMessage(new DummyMessage(5, 2));
			scheduler.forwardMessage(new DummyMessage(6, 1, true));
			scheduler.forwardMessage(new DummyMessage(7, 3, true));
			scheduler.forwardMessage(new DummyMessage(8, 4, true));
			scheduler.forwardMessage(new DummyMessage(9, 2, true));

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
			} catch (final Exception e) {

			}

			scheduler.exit();

			// And chech the final queue configuration
			assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 5);
			assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 0);

			// Checking processing order
			final GatewayManager gatewayManager = scheduler.getGatewayManager();
			gatewayManager.showMessageProcessingOrder();
			final List<ProcessingOrder> list = gatewayManager.getMessageProcessingOrder();

			assertTrue("Expected 9 messages proccesed, found  -->" + list.size(), list.size() == 9);

			final Hashtable<Integer, Integer> htchecks = new Hashtable<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				final ProcessingOrder order = (ProcessingOrder) list.get(i);
				final DummyMessage msg = order.getMsg();
				int groupId = msg.getGroupId();
				int gatewayId = order.getGatewayId();

				if (htchecks.get(groupId) == null) {
					htchecks.put(groupId, gatewayId);
				} else {
					int referencedGateway = htchecks.get(groupId);
					// We chech each insertion

					// Utilities.log.debug("Checking
					// [referencedGateway],[groupId]-->["+referencedGateway+"]["+groupId+"]-->"+order
					// );

					assertTrue(
							"Expected Gateway [" + referencedGateway + "]  for groupId--> " + groupId + "  found -->"
									+ order.getGatewayId() + " instead in order-->" + order + order,
							referencedGateway == order.getGatewayId());
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void multipleGatewayEvenFisrtExampleTest() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(2,
					"scheduler.queue.EvenGroupIdFirstQueueManager");
			final AbstractQueueManager queue = scheduler.getQueue();

			scheduler.forwardMessage(new DummyMessage(1, 1));
			scheduler.forwardMessage(new DummyMessage(2, 3));
			scheduler.forwardMessage(new DummyMessage(3, 5, true));
			scheduler.forwardMessage(new DummyMessage(4, 2));
			scheduler.forwardMessage(new DummyMessage(5, 2));
			scheduler.forwardMessage(new DummyMessage(6, 1, true));
			scheduler.forwardMessage(new DummyMessage(7, 3, true));
			scheduler.forwardMessage(new DummyMessage(8, 4, true));
			scheduler.forwardMessage(new DummyMessage(9, 2, true));

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
			} catch (final Exception e) {

			}

			scheduler.exit();

			// And chech the final queue configuration
			assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 5);
			assertTrue("Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 0);

			// Checking processing order
			final GatewayManager gatewayManager = scheduler.getGatewayManager();
			gatewayManager.showMessageProcessingOrder();
			final List<ProcessingOrder> list = gatewayManager.getMessageProcessingOrder();

			assertTrue("Expected 9 messages proccesed, found  -->" + list.size(), list.size() == 9);

			final Hashtable<Integer, Integer> htchecks = new Hashtable<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				final ProcessingOrder order = (ProcessingOrder) list.get(i);
				final DummyMessage msg = order.getMsg();
				final int groupId = msg.getGroupId();
				final int gatewayId = order.getGatewayId();

				if (htchecks.get(groupId) == null) {
					htchecks.put(groupId, gatewayId);
				} else {
					final int referencedGateway = htchecks.get(groupId);
					// We chech each insertion

					// Utilities.log.debug("Checking
					// [referencedGateway],[groupId]-->["+referencedGateway+"]["+groupId+"]-->"+order
					// );

					assertTrue(
							"Expected Gateway [" + referencedGateway + "]  for groupId--> " + groupId + "  found -->"
									+ order.getGatewayId() + " instead in order-->" + order + order,
							referencedGateway == order.getGatewayId());
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
