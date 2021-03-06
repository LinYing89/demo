package com.example.demo.data;

import com.example.demo.data.device.Device;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据地址
 * @author 44489
 *
 */
@Entity
public class DataAddress {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //地址编号
    private int num;

    private String name;

    @ManyToOne
    private CollectorTerminal collectorTerminal;

    @OneToMany(mappedBy = "dataAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> listDevice = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CollectorTerminal getCollectorTerminal() {
        return collectorTerminal;
    }

    public void setCollectorTerminal(CollectorTerminal collectorTerminal) {
        this.collectorTerminal = collectorTerminal;
    }

    public List<Device> getListDevice() {
        return listDevice;
    }

    public void setListDevice(List<Device> listDevice) {
        this.listDevice = listDevice;
    }

    public void addDevice(Device device) {
        if(null != device) {
            device.setDataAddress(this);
            listDevice.add(device);
        }
    }

    public Device findDevice(String deviceNum) {
        Device dev = null;
        for(Device d : listDevice) {
            if(d.getCoding().equals(deviceNum)) {
                dev = d;
            }
        }
        return dev;
    }

    public void handler(byte[] by) {
        if(null == by || by.length < 1) {
            return;
        }
        if(num == 0x200) {
            byte byData = by[1];
            //门禁的值相反,值1表示门开,报警,值0表示门关,
            //这里将其反过来,与其他设备处理方法一致,但分析数据时要注意值得相反
            float a1Value = byData & 1;
            a1Value = a1Value == 0 ? 1 : 0;
            findDevice("a1").setValue(a1Value);

            findDevice("a3").setValue(byData >> 1 & 1);
            findDevice("a2").setValue(byData >> 2 & 1);
            findDevice("d1").setValue(byData >> 5 & 1);
            findDevice("d2").setValue(byData >> 6 & 1);
            findDevice("d3").setValue(byData >> 7 & 1);
        }else if(getCollectorTerminal().getNum() ==0x21 && num == 0) {
            if(by.length >= 2) {
                findDevice("c1").setValue(bytesToInt(new byte[] {by[0], by[1]}) / 100f);
            }
            if(by.length >= 4) {
                findDevice("c2").setValue(bytesToInt(new byte[] {by[2], by[3]}) / 100f);
            }
        }else if(getCollectorTerminal().getNum() ==0x7c && num == 0){
            getListDevice().get(0).handler(by);
        }
    }

    public int bytesToInt(byte[] by) {
        int value = 0;
        int j = 0;
        for(int i = by.length - 1; i >= 0; i--, j++) {
            value |= ((by[j] & 0xff) << (i * 8));
        }
        return value;
    }

    public static void main(String[] args) {
        DataAddress da = new DataAddress();
        byte[] by = new byte[] {0x0, 0x0, 0x01, 0x01};
        int i = da.bytesToInt(by);
        System.out.println(i+"?");
    }
}