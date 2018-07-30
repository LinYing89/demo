package com.example.demo.communication;

import com.example.demo.data.Untils;
import com.example.demo.data.device.ValueTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyOnTriggedChangedListener implements ValueTrigger.OnTriggedChangedListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onTriggedChanged(ValueTrigger trigger, boolean trigged) {
        logger.info(trigger.getInfo() + trigger.getCompareSymbol() + trigged);
        byte[] by;
        if(trigged) {
            //触发了
            byte action = 0;
            if(trigger.getTargetValue() == 0) {
                action = (byte) 0xff;
            }
            by = new byte[] {0, 07, 00, 00, 00, 04, 00, 00, 05, 00, 05, action, 00};
            byte[] byMsg = Untils.createByteMsg(by);
            if (null != byMsg) {
                logger.info(ServerHandler.bytesToHexString(byMsg));
                ServerHandler.send(byMsg);
            }
        }else {
        }
    }

}