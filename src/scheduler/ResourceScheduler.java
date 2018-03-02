package scheduler;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.GatewayManager;
import scheduler.observer.*;
import scheduler.queue.*;

public class ResourceScheduler {

	private AbstractQueueManager queueManager;

	private GatewayManager gatewayManager;

	private MessageObserver observer;

	// Attribute for the Cancellation Feature
	private final HashMap hmCacelledGroupId = new HashMap<>();

	private long proccesedMessages = 0;

	public ResourceScheduler() {
		this(1, null);
	}

	public ResourceScheduler(final int resources) {
		this(resources, null);
	}

	public ResourceScheduler(final String strategyClassName) {
		this(1, strategyClassName);
	}

	public ResourceScheduler(final int resources, final String strategyClassName) {
		super();

		// The object dependencies are clearly exposed here.
		gatewayManager = new GatewayManager(resources);
		Utilities.log.debug(getClass().getName() + ":: !!! strategyClassName-->" + strategyClassName);
		queueManager = instanciateQueueManager(strategyClassName, gatewayManager);
		observer = new DefaultMessageObserver(queueManager, gatewayManager);

		// Runnable object started
		final Thread queueThread = new Thread(queueManager);
		queueThread.start();
	}

	public AbstractQueueManager getQueue() {
		return queueManager;
	}

	public GatewayManager getGatewayManager() {
		return gatewayManager;
	}

	public void exit() {
		while (queueManager.getMessageCount() > 0) {
			try {
				Thread.sleep(Utilities.PROCESSING_DELAY);
			} catch (final Exception e) {
			}
		}
		if (gatewayManager != null) {
			gatewayManager.exit();
		}
		if (queueManager != null) {
			queueManager.exit();
		}
	}

	public void forwardMessage(final DummyMessage msg) throws TerminatedGroupException {
		Utilities.log.error(getClass().getName() + ":: forwardMessage-->" + msg);
		if (msg == null) {
			Utilities.log.error(getClass().getName() + ":: Error null null Message...");
		} else if (queueManager == null) {
			Utilities.log.error(getClass().getName() + ":: Error queueManager found...");
		} else if (observer == null) {
			Utilities.log.error(getClass().getName() + ":: Error null observer found...");
		} else if (isCancelledGroupId(msg.getGroupId())) {
			Utilities.log.error(getClass().getName() + ":: Error the groupId of the message " + msg
					+ " has been cancelled. The Message will not be proccessed...");
		} else if (queueManager.isTerminated(msg.getGroupId())) {
			Utilities.log.error(getClass().getName() + ":: Error the groupId of the message " + msg
					+ " has been Terminated. The Message will not be proccessed...");
			// Raise an Exception instead of a simple error...
			throw new TerminatedGroupException(
					"Message " + msg + " for terminated groupId-->" + msg.getGroupId() + " found");
		} else {
			final BlockingQueue<DummyMessage> channelToQueueManager = queueManager.getChannelFromProducer();
			if (channelToQueueManager == null) {
				Utilities.log.error(getClass().getName() + ":: Error null channelToQueueManager...");
			} else
				try {
					msg.setObserver(observer);
					Utilities.log.debug(getClass().getName() + ":: Putting-->" + msg + " into channelToQueueManager");
					channelToQueueManager.put(msg);
					proccesedMessages++;
				} catch (final Exception e) {
					Utilities.log.error(getClass().getName() + ":: Exception sending msg-->" + msg);
					e.printStackTrace();
				}
		}
	}

	/****************************************/

	// Method for the Cancellation Feature
	public void cancelGroupId(final int groupId) {
		Utilities.log.debug(getClass().getName() + ":: cancelIdGroup(" + groupId + ")");
		if (hmCacelledGroupId.get(groupId) == null) {
			hmCacelledGroupId.put(groupId, groupId);
			Utilities.log.debug(getClass().getName() + ":: removing remaining Messages from queueManager...");
			queueManager.removeAllGroupIdMessagesFromQueue(groupId);
			Utilities.log.debug(getClass().getName() + ":: " + groupId + " cancelled.");
		} else {
			Utilities.log.debug(getClass().getName() + ":: " + groupId + " already cancelled.");
		}
	}

	// Method for the Cancellation Feature
	private boolean isCancelledGroupId(final int groupId) {
		Utilities.log.debug(getClass().getName() + ":: isCancelledIdGroup(" + groupId + ")");
		if (hmCacelledGroupId.get(groupId) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**********************************************************/

	// Strategy Priorizitation feature
	private AbstractQueueManager instanciateQueueManager(final String className, final GatewayManager gatewayManager) {
		Utilities.log.debug(getClass().getName() + ":: instanciateQueueManager(" + className + ",...,...)");
		if (className != null) {
			if (!className.equalsIgnoreCase("")) {
				try {
					final ClassLoader classLoader = AbstractQueueManager.class.getClassLoader();
					final Class aClass = classLoader.loadClass(className);
					Utilities.log.debug(getClass().getName() + ":: Instanciated AbstractQueueManager subclass: "
							+ className + " " + aClass.getName());
					return (AbstractQueueManager) aClass.getDeclaredConstructor(gatewayManager.getClass())
							.newInstance(gatewayManager);
				} catch (final Exception e) {
					Utilities.log.error(getClass().getName()
							+ ":: Exception instantiating AbstractQueueManager subclass: " + className);
					e.printStackTrace();
				}
			} else {
				Utilities.log.error(getClass().getName() + ":: Incorrect className-->: " + className
						+ " Instanciating default class");
			}
		} else {
			Utilities.log.error(
					getClass().getName() + ":: Incorrect className-->: " + className + " Instanciating default class");
		}
		final AbstractQueueManager am = new DefaultQueueManager(gatewayManager);
		Utilities.log.debug(
				getClass().getName() + ":: instanciated AbstractQueueManager subclass: " + am.getClass().getName());
		return am;
	}

	/**********************************************************/

	/* Used in JUnit */

	public long getProcessedMessages() {
		return proccesedMessages;
	}

	/**********************************************************/

	// Basic test
	public static void main(String args[]) {
		try {
			final ResourceScheduler scheduler = new ResourceScheduler(1);
			scheduler.forwardMessage(new DummyMessage(1, 2));
			scheduler.forwardMessage(new DummyMessage(2, 1, true));
			scheduler.forwardMessage(new DummyMessage(3, 2, true));
			scheduler.forwardMessage(new DummyMessage(4, 3, true));

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