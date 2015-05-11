import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ QueueManagerTest.class, GatewayManagerTest.class, BaseTest.class, MultipleGatewayTest.class,ExtrasTest.class })
public class AllTests {

}