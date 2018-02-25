import static org.junit.Assert.assertTrue;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.gateway.GatewayManager;

public class GatewayManagerTest {

	@Test
	public void gatewayIncorrectInitializationTest() {
		final ResourceScheduler scheduler = new ResourceScheduler(-99);
		final GatewayManager gatewayManager = scheduler.getGatewayManager();
		scheduler.exit();
		assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")",
				gatewayManager.getGatewayCount() == 1);
	}

	@Test
	public void gatewayNormalInitializationTest() {
		final ResourceScheduler scheduler = new ResourceScheduler();
		final GatewayManager gatewayManager = scheduler.getGatewayManager();
		scheduler.exit();
		assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")",
				gatewayManager.getGatewayCount() == 1);
	}

	@Test
	public void gatewayMultipleInitializationTest() {
		final ResourceScheduler scheduler = new ResourceScheduler(99);
		final GatewayManager gatewayManager = scheduler.getGatewayManager();
		scheduler.exit();
		assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")",
				gatewayManager.getGatewayCount() == 99);
	}

}
