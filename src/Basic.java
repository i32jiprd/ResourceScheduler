import scheduler.ResourceScheduler;
import scheduler.dummyObjects.DummyMessage;

public class Basic {

	public static void main(String args[]) {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(1);
			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));

			/**********************************************/

			// ResourceScheduler scheduler = new ResourceScheduler(2,
			// "scheduler.queue.EvenGroupIdFirstQueueManager");
			// scheduler.forwardMessage(new DummyMessage(1, 1));
			// scheduler.forwardMessage(new DummyMessage(2, 3));
			// scheduler.forwardMessage(new DummyMessage(3, 5,true));
			// scheduler.forwardMessage(new DummyMessage(4, 2));
			// scheduler.forwardMessage(new DummyMessage(5, 2));
			// scheduler.forwardMessage(new DummyMessage(6, 1, true));
			// scheduler.forwardMessage(new DummyMessage(7, 3, true));
			// scheduler.forwardMessage(new DummyMessage(8, 4, true));
			// scheduler.forwardMessage(new DummyMessage(9, 2, true));

			/**********************************************/

			// ResourceScheduler scheduler = new ResourceScheduler(3);
			// scheduler.forwardMessage(new DummyMessage(1, 1));
			// scheduler.forwardMessage(new DummyMessage(2, 3));
			// scheduler.forwardMessage(new DummyMessage(3, 5,true));
			// scheduler.forwardMessage(new DummyMessage(4, 2));
			// scheduler.forwardMessage(new DummyMessage(5, 2));
			// scheduler.forwardMessage(new DummyMessage(6, 1, true));
			// scheduler.forwardMessage(new DummyMessage(7, 3, true));
			// scheduler.forwardMessage(new DummyMessage(8, 4, true));
			// scheduler.forwardMessage(new DummyMessage(9, 2, true));

			/**********************************************/

			// ResourceScheduler scheduler = new ResourceScheduler(3,
			// "scheduler.queue.EvenGroupIdFirstQueueManager");
			// scheduler.forwardMessage(new DummyMessage(1, 1));
			// scheduler.forwardMessage(new DummyMessage(2, 3));
			// scheduler.forwardMessage(new DummyMessage(3, 2));
			// scheduler.forwardMessage(new DummyMessage(4, 5, true));
			// scheduler.forwardMessage(new DummyMessage(5, 2));
			// scheduler.forwardMessage(new DummyMessage(6, 1, true));
			// scheduler.forwardMessage(new DummyMessage(7, 3, true));
			// scheduler.forwardMessage(new DummyMessage(8, 4, true));
			// scheduler.forwardMessage(new DummyMessage(9, 2, true));

			try {
				Thread.sleep(7000);
			} catch (final Exception e) {
			}
			scheduler.exit();
			scheduler.getGatewayManager().showMessageProcessingOrder();
			System.exit(0);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
