package scheduler.queue;

import java.util.*;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.GatewayManager;

public class DefaultQueueManager extends AbstractQueueManager {

	public DefaultQueueManager(final GatewayManager gatewayManager) {
		super(gatewayManager);
		Utilities.log.debug(getClass().getName() + ":: DefaultQueueManager(...)");
	}

	@Override
	public DummyMessage getAternativeMsgForGateway() {
		Utilities.log.debug(getClass().getName() + ":: getAternativeMsgForGateway()");
		DummyMessage nextMsg;
		final List<GroupIdQueue> list = geNotEmptyLists();
		if (!list.isEmpty()) {
			for (final GroupIdQueue group : list) {
				if (!gatewayManager.isGroupIdBeingSent(group.getId())) {
					nextMsg = group.extractMessage();
					Utilities.log.debug(getClass().getName() + ":: !!! Found-->" + nextMsg);
					return nextMsg;
				} else {
					Utilities.log.debug(getClass().getName() + ":: !!! Group being used-->" + group.getId());
				}
			}
		}
		return null;
	}

	@Override
	public DummyMessage extractMessage() {
		Utilities.log.debug(getClass().getName() + ":: extractMessage()");
		if (!queue.isEmpty()) {
			final Iterator<GroupIdQueue> iterator = queue.iterator();
			while (iterator.hasNext()) {
				final GroupIdQueue group = iterator.next();
				if (group.getMessagesCount() > 0) {
					final DummyMessage msg = group.extractMessage();
					if (msg != null) {
						Utilities.log.debug(getClass().getName() + ":: returning-->" + msg);
						return msg;
					}
				}
			}
		}
		Utilities.log.debug(getClass().getName() + ":: returning-->null");
		return null;
	}

	@Override
	public DummyMessage extractNextMessageForGroupId(final int groupId) {
		Utilities.log.debug(getClass().getName() + ":: getNextGroupIdMessage(" + groupId + ")");
		if (!queue.isEmpty()) {
			final GroupIdQueue group = searchGroupId(groupId);
			if (group != null) {
				if (group.getMessagesCount() > 0) {
					final DummyMessage msg = group.extractMessage();
					Utilities.log.debug(getClass().getName() + ":: returning-->" + msg);
					return msg;
				}
			}
		}
		Utilities.log.debug(getClass().getName() + ":: returning-->null");
		return null;
	}

}