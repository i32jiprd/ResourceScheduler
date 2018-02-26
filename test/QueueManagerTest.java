import static org.junit.Assert.assertTrue;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.queue.AbstractQueueManager;

public class QueueManagerTest {

	@Test
	public void storeMessageAndQueuesCount() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler();
			scheduler.getQueue().setMessageProcessingEnabled(false);

			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));

			final AbstractQueueManager queue = scheduler.getQueue();
			queue.showCurrentQueueStatus();

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.PROCESSING_DELAY * scheduler.getProcessedMessages());
			} catch (final Exception e) {

			}

			assertTrue("Invalid number of lists in queue (" + queue.getListCount() + ")", queue.getListCount() == 3);
			assertTrue("Invalid number of Messages in queue-->" + queue.getMessageCount(),
					queue.getMessageCount() == 4);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addAndRetrieveTest() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler();
			scheduler.getQueue().setMessageProcessingEnabled(false);

			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.WAITING_DELAY);
			} catch (final Exception e) {

			}

			final AbstractQueueManager queue = scheduler.getQueue();

			Utilities.log.debug("Starting Message extraction...");
			queue.showCurrentQueueStatus();

			DummyMessage msg = queue.extractMessage();
			queue.showCurrentQueueStatus();
			assertTrue("Step 1 Invalid msgFound-->" + msg + " Expected id: 1 GroupId: 2",
					msg.getId() == 1 && msg.getGroupId() == 2);

			msg = queue.extractMessage();
			queue.showCurrentQueueStatus();
			assertTrue("Step 2 Invalid msgFound-->" + msg + " Expected id: 3 GroupId: 2",
					msg.getId() == 3 && msg.getGroupId() == 2);

			msg = queue.extractMessage();
			queue.showCurrentQueueStatus();
			assertTrue("Step 3 Invalid msgFound-->" + msg + " Expected id: 2 GroupId: 1",
					msg.getId() == 2 && msg.getGroupId() == 1);

			msg = queue.extractMessage();
			queue.showCurrentQueueStatus();
			assertTrue("Step 4 Invalid msgFound-->" + msg + " Expected id: 4 GroupId: 3",
					msg.getId() == 4 && msg.getGroupId() == 3);

			msg = queue.extractMessage();
			queue.showCurrentQueueStatus();
			assertTrue("Step 5 Invalid msgFound-->" + msg + " Expected null message", msg == null);

			scheduler.exit();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nullMessagetSendTest() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler();
			scheduler.getQueue().setMessageProcessingEnabled(false);
			final AbstractQueueManager queue = scheduler.getQueue();

			scheduler.forwardMessage(null);
			queue.showCurrentQueueStatus();

			assertTrue("Step 1 Invalid number of lists in queue (" + queue.getListCount() + ")",
					queue.getListCount() == 0);
			assertTrue("Step 1 Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 0);

			scheduler.forwardMessage(new DummyMessage(1, 1));
			queue.showCurrentQueueStatus();

			// We wait a bit to the message to be enqueued
			try {
				Thread.sleep(Utilities.WAITING_DELAY);
			} catch (final Exception e) {

			}

			assertTrue("Step 2 Invalid number of lists in queue (" + queue.getListCount() + ")",
					queue.getListCount() == 1);
			assertTrue("Step 2 Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 1);

			scheduler.forwardMessage(null);
			queue.showCurrentQueueStatus();

			// We wait a bit to the message to be enqueued
			try {
				Thread.sleep(Utilities.WAITING_DELAY);
			} catch (final Exception e) {

			}

			assertTrue("Step 3 Invalid number of lists in queue (" + queue.getListCount() + ")",
					queue.getListCount() == 1);
			assertTrue("Step 3 Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 1);

			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			// We wait a bit to the message to be enqueued
			try {
				Thread.sleep(Utilities.WAITING_DELAY);
			} catch (final Exception e) {

			}

			assertTrue("Step 4 Invalid number of lists in queue (" + queue.getListCount() + ")",
					queue.getListCount() == 1);
			assertTrue("Step 4 Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 2);

			DummyMessage msg = queue.extractMessage();
			assertTrue("Step 5 Invalid msgFound-->" + msg + " Expected id: 1 GroupId: 1",
					msg.getId() == 1 && msg.getGroupId() == 1);

			msg = queue.extractMessage();
			assertTrue("Step 6 Invalid msgFound-->" + msg + " Expected id: 2 GroupId: 1",
					msg.getId() == 2 && msg.getGroupId() == 1);

			msg = queue.extractMessage();
			assertTrue("Step 7 Invalid msgFound-->" + msg, msg == null);

			scheduler.exit();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void extractMessagesInOrder() {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler();
			scheduler.getQueue().setMessageProcessingEnabled(false);

			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));

			// We wait a bit to Messages to be enqueued
			try {
				Thread.sleep(Utilities.WAITING_DELAY);
			} catch (final Exception e) {

			}

			final AbstractQueueManager queue = scheduler.getQueue();
			queue.showCurrentQueueStatus();

			DummyMessage msg = queue.extractNextMessageForGroupId(3);
			queue.showCurrentQueueStatus();
			assertTrue("Step 1 Invalid msgFound-->" + msg + " Expected id: 4 GroupId: 3",
					msg.getId() == 4 && msg.getGroupId() == 3);

			msg = queue.extractNextMessageForGroupId(1);
			queue.showCurrentQueueStatus();
			assertTrue("Step 2 Invalid msgFound-->" + msg + " Expected id: 2 GroupId: 1",
					msg.getId() == 2 && msg.getGroupId() == 1);

			msg = queue.extractNextMessageForGroupId(3);
			queue.showCurrentQueueStatus();
			assertTrue("Step 3 Invalid msgFound-->" + msg + " Expected null value", msg == null);

			msg = queue.extractNextMessageForGroupId(2);
			queue.showCurrentQueueStatus();
			assertTrue("Step 4 Invalid msgFound-->" + msg, msg.getId() == 1 && msg.getGroupId() == 2);

			msg = queue.extractNextMessageForGroupId(2);
			queue.showCurrentQueueStatus();
			assertTrue("Step 5 Invalid msgFound-->" + msg, msg.getId() == 3 && msg.getGroupId() == 2);

			msg = queue.extractNextMessageForGroupId(2);
			queue.showCurrentQueueStatus();
			assertTrue("Step 6 Invalid msg expected null Found-->" + msg, msg == null);

			scheduler.exit();

			assertTrue("Step 7 Invalid number of Messages registered-->" + queue.getMessageCount(),
					queue.getMessageCount() == 0);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
