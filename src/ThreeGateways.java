import scheduler.ResourceScheduler;
import scheduler.dummyObjects.DummyMessage;

public class ThreeGateways {

    public static void main(String args[]) {
	try {

	    ResourceScheduler scheduler = new ResourceScheduler(3);
	    scheduler.forwardMessage(new DummyMessage(1, 1));
	    scheduler.forwardMessage(new DummyMessage(2, 3));
	    scheduler.forwardMessage(new DummyMessage(3, 5, true));
	    scheduler.forwardMessage(new DummyMessage(4, 2));
	    scheduler.forwardMessage(new DummyMessage(5, 2));
	    scheduler.forwardMessage(new DummyMessage(6, 1, true));
	    scheduler.forwardMessage(new DummyMessage(7, 3, true));
	    scheduler.forwardMessage(new DummyMessage(8, 4, true));
	    scheduler.forwardMessage(new DummyMessage(9, 2, true));
	    try {
		Thread.sleep(7000);
	    } catch (Exception e) {
	    }
	    scheduler.exit();
	    scheduler.getGatewayManager().showMessageProcessingOrder();
	    System.exit(0);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
