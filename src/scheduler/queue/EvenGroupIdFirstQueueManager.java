package scheduler.queue;

import java.util.*;

import scheduler.Util.Utilities;
import scheduler.dummyObjects.DummyMessage;
import scheduler.gateway.GatewayManager;

public class EvenGroupIdFirstQueueManager extends AbstractQueueManager {

	public EvenGroupIdFirstQueueManager(final GatewayManager gatewayManager) {
		super(gatewayManager);
		Utilities.log.debug(getClass().getName() + ":: EvenGroupIdFirstQueueManager(...)");
	}

	private DummyMessage getDefault() {
		Utilities.log.debug(getClass().getName() + ":: No even groupId found trying other options...");
		final Iterator<GroupIdQueue> iterator = queue.iterator();
		while (iterator.hasNext()) {
			final GroupIdQueue group = iterator.next();
			if (group.getMessagesCount() > 0) {
				final DummyMessage msg = group.extractMessage();
				if (msg != null) {
					Utilities.log.debug(getClass().getName() + ":: returning even groupId Message-->" + msg);
					return msg;
				}
			}
		}
		return null;
	}

	@Override
	public DummyMessage getAternativeMsgForGateway() {
		Utilities.log.debug(getClass().getName() + ":: getAternativeMsgForGateway()");
		DummyMessage nextMsg = null;
		final List<GroupIdQueue> list = geNotEmptyLists();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				final GroupIdQueue group = list.get(i);
				if (group.getId() % 2 == 0) {
					if (!gatewayManager.isGroupIdBeingSent(group.getId())) {
						nextMsg = group.extractMessage();
						Utilities.log.debug(getClass().getName() + ":: !!! Found-->" + nextMsg);
						return nextMsg;
					} else {
						Utilities.log.debug(getClass().getName() + ":: !!! Group being used-->" + group.getId());
					}
				}
			}
		}

		return getDefault();
	}

	@Override
	public DummyMessage extractNextMessageForGroupId(int groupId) {
		Utilities.log.debug(getClass().getName() + ":: (2)getNextGroupIdMessage(" + groupId + ")");
		if (!queue.isEmpty()) {
			final GroupIdQueue group = searchGroupId(groupId);
			if (group != null) {
				if (group.getMessagesCount() > 0) {
					if (group.getId() == groupId || group.getId() % 2 == 0) {
						final DummyMessage msg = group.extractMessage();
						Utilities.log.debug(getClass().getName() + ":: returning Even groupIdMessage-->" + msg);
						return msg;
					}
				}
			}
		}
		Utilities.log.debug(getClass().getName() + ":: returning-->null");
		return null;
	}

	@Override
	public DummyMessage extractMessage() {
		Utilities.log.debug(getClass().getName() + ":: extractMessage()");
		Utilities.log.debug(getClass().getName() + ":: Proccesing even groupIds()");
		if (!queue.isEmpty()) {
			final Iterator<GroupIdQueue> iterator = queue.iterator();
			while (iterator.hasNext()) {
				final GroupIdQueue group = iterator.next();
				if (group.getMessagesCount() > 0) {
					if (group.getId() % 2 == 0) {
						final DummyMessage msg = group.extractMessage();
						if (msg != null) {
							Utilities.log.debug(getClass().getName() + ":: returning even groupId Message-->" + msg);
							return msg;
						}
					}
				}
			}
			return getDefault();
		}
		Utilities.log.debug(getClass().getName() + ":: returning-->null");
		return null;
	}

}