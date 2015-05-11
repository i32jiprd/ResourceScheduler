package scheduler.Util;

import scheduler.dummyObjects.DummyMessage;

public class ProcessingOrder {

    private int gatewayId=-1;
    
    private DummyMessage msg=null;
    
    public ProcessingOrder(int gatewayId,DummyMessage msg){
	this.gatewayId=gatewayId;
	this.msg=msg;
    }
    
    public int getGatewayId() {
        return this.gatewayId;
    }

    public DummyMessage getMsg() {
        return this.msg;
    }

    @Override
    public String toString(){
	return "Gateway["+gatewayId+"] Message-->"+msg;
    }
    
    
}
