package com.example.demo.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户组
 */
@Entity
public class DevGroup {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;

    //昵称
    private String petName;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "devGroup", cascade = CascadeType.ALL)
    private List<MsgManager> msgManagers = new ArrayList<>();

    //经度
    private double longitude;
    //纬度
    private double latitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<MsgManager> getMsgManagers() {
        return msgManagers;
    }

    public void setMsgManagers(List<MsgManager> msgManagers) {
        this.msgManagers = msgManagers;
    }

    public void addMsgManager(MsgManager msgManager){
        if(null != msgManager) {
            if (!msgManagers.contains(msgManager)) {
                msgManagers.add(msgManager);
            }
        }
    }

    public boolean removeMsgManager(MsgManager msgManager){
        if(null != msgManager){
            return msgManagers.remove(msgManager);
        }
        return false;
    }

    public MsgManager removeMsgManager(int index){
        if(index > 0 && index < msgManagers.size()){
            return msgManagers.remove(index);
        }
        return null;
    }
}
