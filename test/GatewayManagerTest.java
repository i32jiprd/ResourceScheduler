import static org.junit.Assert.assertTrue;

import org.junit.Test;

import scheduler.ResourceScheduler;
import scheduler.gateway.GatewayManager;

public class GatewayManagerTest {

    @Test
    public void gatewayIncorrectInitializationTest() {
	ResourceScheduler scheduler = new ResourceScheduler(-99);
	GatewayManager gatewayManager = scheduler.getGatewayManager();
	scheduler.exit();
	assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")", gatewayManager.getGatewayCount() == 1);
    }

    @Test
    public void gatewayNormalInitializationTest() {
	ResourceScheduler scheduler = new ResourceScheduler();
	GatewayManager gatewayManager = scheduler.getGatewayManager();
	scheduler.exit();
	assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")", gatewayManager.getGatewayCount() == 1);
    }

    @Test
    public void gatewayMultipleInitializationTest() {
	ResourceScheduler scheduler = new ResourceScheduler(99);
	GatewayManager gatewayManager = scheduler.getGatewayManager();
	scheduler.exit();
	assertTrue("Invalid number of DummyGatewayInstances (" + gatewayManager.getGatewayCount() + ")", gatewayManager.getGatewayCount() == 99);
    }
    
}
