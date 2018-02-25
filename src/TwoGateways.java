import scheduler.ResourceScheduler;
import scheduler.dummyObjects.DummyMessage;

public class TwoGateways {

	public static void main(String args[]) {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(2);
			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));
			scheduler.forwardMessage(new DummyMessage(5, 5, true));
			scheduler.forwardMessage(new DummyMessage(6, 7));
			scheduler.forwardMessage(new DummyMessage(7, 4));
			scheduler.forwardMessage(new DummyMessage(8, 7, true));
			scheduler.forwardMessage(new DummyMessage(9, 4, true));
			scheduler.forwardMessage(new DummyMessage(10, 6, true));

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
