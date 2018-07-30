package com.example.demo.data.device;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DevSwitch")
public class DevSwitch extends Device {

    public DevSwitch() {
        // TODO Auto-generated constructor stub
    }

    public DevSwitch(String coding) {
        super(coding);
        // TODO Auto-generated constructor stub
    }

    public DevSwitch(String coding, String name) {
        super(coding, name);
        // TODO Auto-generated constructor stub
    }

    public byte getAction() {
        byte action;
        if (getValue() == 0) {
            action = 0;
        } else {
            action = (byte) 0xff;
        }
        return action;
    }

}