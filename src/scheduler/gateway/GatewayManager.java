package scheduler.gateway;

import java.util.*;

import scheduler.Util.*;
import scheduler.dummyObjects.DummyMessage;

public class GatewayManager {

	// We need a synchronized version of ArrayList to store data
	private final List<DummyGateway> gateways = Collections.synchronizedList(new ArrayList<DummyGateway>());

	public final List<ProcessingOrder> messageProcessingOrder = Collections
			.synchronizedList(new ArrayList<ProcessingOrder>());

	public GatewayManager(int instances) {
		Utilities.log.debug(getClass().getName() + ":: GatewayManager(" + instances + ")");
		if (instances < 0) {
			Utilities.log.debug(getClass().getName() + ":: Invalid instances value (" + instances
					+ "). Assuming 1 GatewayManager instance");
			instances = 1;
		}
		Utilities.log.debug(getClass().getName() + ":: Creating " + instances + " GatewayManager instance");
		for (int i = 0; i < instances; i++) {
			final DummyGateway gateway = new DummyGateway(i, this);
			gateways.add(gateway);
			new Thread(gateway).start();
		}
	}

	public DummyGateway getGatewaySending(int groupId) {
		Utilities.log.debug(getClass().getName() + ":: getGatewaySending(" + groupId + ")");
		if (gateways != null) {
			for (final DummyGateway dummyGateway : gateways) {
				final DummyMessage msg = dummyGateway.getLastMessageSent();
				if (msg != null) {
					if (msg.getGroupId() == groupId) {
						return dummyGateway;
					}
				}
			}
		} else {
			Utilities.log.error(getClass().getName() + ":: Error null gateways found...");
		}
		return null;
	}

	// Checks the list of groupId being send
	public boolean isGroupIdBeingSent(int groupId) {
		Utilities.log.debug(getClass().getName() + ":: isGroupIdBeingSent(" + groupId + ")");
		if (gateways != null) {
			for (final DummyGateway dummyGateway : gateways) {
				final DummyMessage msg = dummyGateway.getLastMessageSent();
				if (msg != null) {
					Utilities.log.debug(getClass().getName() + ":: Checking msg.getGroupId()-->" + msg.getGroupId()
							+ " and " + groupId);
					if (msg.getGroupId() == groupId) {
						return true;
					}
				} else {
					Utilities.log.warn(getClass().getName() + ":: null Message...");
				}
			}
		} else {
			Utilities.log.warn(getClass().getName() + ":: null gateways found...");
		}
		return false;
	}

	public void exit() {
		if (gateways != null) {
			for (final DummyGateway dummyGateway : gateways) {
				dummyGateway.exit();
			}
		} else {
			Utilities.log.error(getClass().getName() + ":: Error null gateways found...");
		}
	}

	private boolean isAnyGatewayProcessingGroupId(int groupId) {
		Utilities.log.debug(getClass().getName() + ":: isAnyGatewayProcessingGroupId(" + groupId + ")");
		if (gateways != null) {
			for (final DummyGateway dummyGateway : gateways) {
				final DummyMessage msg = dummyGateway.getLastMessageSent();
				if (msg != null) {
					if (msg.getGroupId() == groupId) {
						Utilities.log.debug(getClass().getName() + "::!!! true for-->" + dummyGateway);
						return true;
					}
				}
			}
		} else {
			Utilities.log.error(getClass().getName() + ":: Error null gateways found...");
		}
		Utilities.log.debug(getClass().getName() + "::!!! false for all gateways");
		return false;
	}

	public DummyGateway getAvailableGatewayForGroupId(int groupId) {
		Utilities.log.debug(getClass().getName() + ":: getAvailableGatewayForGroupIdTEST(" + groupId + ")");
		// We left the correct gateway to process the whole groupId serie
		if (!isAnyGatewayProcessingGroupId(groupId)) {
			for (final DummyGateway dummyGateway : gateways) {
				final DummyMessage msg = dummyGateway.getLastMessageSent();
				if (dummyGateway.isAvailable()) {
					return dummyGateway;
				} else {
					Utilities.log.debug(getClass().getName() + ":: !!! No Available Gateway-->" + dummyGateway);
				}
			}
		}
		return null;
	}

	/* Used in JUnit tests */

	public void showMessageProcessingOrder() {
		StringBuffer aux = new StringBuffer("showMessageProcessingOrder()" + Utilities.LINE_SEPARATOR);
		aux.append("/---------START OF LIST ----------/" + Utilities.LINE_SEPARATOR);
		if (messageProcessingOrder.isEmpty()) {
			aux.append("Empty List !!!" + Utilities.LINE_SEPARATOR);
		} else {
			for (final ProcessingOrder processingOrder : messageProcessingOrder) {
				aux.append(processingOrder + Utilities.LINE_SEPARATOR);
			}
		}
		aux.append("/---------END OF LIST-------------/" + Utilities.LINE_SEPARATOR);
		aux.append("Items-->" + messageProcessingOrder.size() + Utilities.LINE_SEPARATOR);
		Utilities.log.debug(aux.toString());
	}

	public int getGatewayCount() {
		return gateways.size();
	}

	public List<ProcessingOrder> getMessageProcessingOrder() {
		return messageProcessingOrder;
	}

}