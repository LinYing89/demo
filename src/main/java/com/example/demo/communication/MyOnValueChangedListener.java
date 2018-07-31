package com.example.demo.communication;

import com.example.demo.SpringUtil;
import com.example.demo.data.AlarmInfo;
import com.example.demo.data.HistoryInfo;
import com.example.demo.data.device.DevAlarm;
import com.example.demo.data.device.Device;
import com.example.demo.data.device.ValueData;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.HistoryInfoRepository;
import com.example.demo.service.ValueChangedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyOnValueChangedListener implements Device.OnValueChangedListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DeviceRepository deviceRepository = SpringUtil.getBean(DeviceRepository.class);
    private HistoryInfoRepository historyInfoRepository = SpringUtil.getBean(HistoryInfoRepository .class);
    private ValueChangedService valueChangedService = SpringUtil.getBean(ValueChangedService.class);

    @Override
    public void onValueChanged(Device device, float value) {
        logger.info("值改变:" + device.getCoding() + " value:" + value);

        //添加历史记录
        HistoryInfo hi = new HistoryInfo(new Date(), device, value);

        historyInfoRepository.save(hi);

        //发送到浏览器
        ValueData valueData = new ValueData();
        valueData.setCoding(device.getCoding());
        valueData.setValue(value);
        valueChangedService.broadcastValueChanged(valueData);

        if(device instanceof DevAlarm) {
            AlarmInfo ai = new AlarmInfo();
            ai.setAlarmTime(new Date());
            if(value == 0) {
                ai.setInfo(device.getName() + " 报警");
            }else {
                ai.setInfo(device.getName() + " 报警解除");
            }
            device.addAlarmInfo(ai);
            Collections.sort(device.getListAlarmInfo());
            deviceRepository.save(device);
        }
    }

}