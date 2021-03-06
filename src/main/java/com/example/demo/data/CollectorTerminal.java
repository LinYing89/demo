package com.example.demo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 采集终端
 * @author 44489
 *
 */
@Entity
public class CollectorTerminal {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //采集终端号
    private int num;

    private String name;

    @ManyToOne
    private Omnibus omnibus;

    @OneToMany(mappedBy = "collectorTerminal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataAddress> listDataAddress = new ArrayList<>();

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

    public Omnibus getOmnibus() {
        return omnibus;
    }

    public void setOmnibus(Omnibus omnibus) {
        this.omnibus = omnibus;
    }

    public List<DataAddress> getListDataAddress() {
        return listDataAddress;
    }

    public void setListDataAddress(List<DataAddress> listDataAddress) {
        this.listDataAddress = listDataAddress;
    }

    public void addDataAddress(DataAddress dataAddress) {
        if(null != dataAddress) {
            dataAddress.setCollectorTerminal(this);
            listDataAddress.add(dataAddress);
        }
    }

    public DataAddress findDataAddress(int addrNum) {
        DataAddress address = null;
        for(DataAddress a : listDataAddress) {
            if(a.getNum() == addrNum) {
                address = a;
            }
        }
        return address;
    }

    public void handler(byte[] by) {
        int addrNum = by[0] << 8 | by[1];
        DataAddress addr = findDataAddress(addrNum);
        if(addr == null) {
            Logger logger = LoggerFactory.getLogger(this.getClass().getName());
            logger.error("数据地址不存在: num:" + addrNum);
            return;
        }
        int dataLen = by[2] << 8 | by[3];
        if(dataLen == 0) {
            return;
        }
        byte[] byData = Arrays.copyOfRange(by, 4, dataLen + 4);
        addr.handler(byData);
    }
}
