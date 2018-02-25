package scheduler.observer;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.*;
import scheduler.queue.AbstractQueueManager;

public class DefaultMessageObserver implements MessageObserver {

	private AbstractQueueManager queueManager = null;

	private GatewayManager gatewayManager = null;

	public DefaultMessageObserver(final AbstractQueueManager queueManager, final GatewayManager gatewayManager) {
		this.queueManager = queueManager;
		this.gatewayManager = gatewayManager;
	}

	@Override
	public void notifyObserver(final DummyMessage msg) {
		Utilities.log.debug(getClass().getName() + ":: notifyObserver(" + msg + ")");
		if (queueManager == null) {
			Utilities.log.error(getClass().getName() + ":: null queueManager found");
		} else if (gatewayManager == null) {
			Utilities.log.error(getClass().getName() + ":: null gatewayManager found");
		} else if (msg == null) {
			Utilities.log.error(getClass().getName() + ":: null Message received");
		} else {
			// We search the Gateway Processing the received message
			final DummyGateway gateway = gatewayManager.getGatewaySending(msg.getGroupId());
			if (gateway == null) {
				Utilities.log.error(getClass().getName() + ":: null gateway found");
			} else {
				DummyMessage nextMsg = queueManager.extractNextMessageForGroupId(msg.getGroupId());
				if (nextMsg == null) {
					nextMsg = queueManager.getAternativeMsgForGateway();
				}
				queueManager.showCurrentQueueStatus();
				if (nextMsg != null) {
					try {
						Utilities.log.debug(getClass().getName() + ":: nextMsg found-->" + nextMsg);
						// We send the nextMessage back
						gateway.getChannel().put(nextMsg);
					} catch (final Exception e) {
					}
				} else {
					Utilities.log.warn(getClass().getName() + ":: No nextMsg found!!! ");
					// We release the gateway
					gateway.releaseLastMessage();
				}
			}
		}
	}

}
