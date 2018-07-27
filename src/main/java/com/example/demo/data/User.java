package com.example.demo.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;

    //昵称
    private String petName;
    private String email;
    private String tel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DevGroup> devGroups = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<DevGroup> getDevGroups() {
        return devGroups;
    }

    public void setDevGroups(List<DevGroup> devGroups) {
        this.devGroups = devGroups;
    }

    /**
     * 根据组id寻找组
     * @param devGroupId 组id
     * @return 组对象
     */
    public DevGroup findDevGroupById(long devGroupId){
        for(DevGroup group : devGroups){
            if(group.getId() == devGroupId){
                return group;
            }
        }
        return null;
    }

    public void addGroup(DevGroup devGroup){
        if(null != devGroup){
            if(!devGroups.contains(devGroup)){
                devGroup.setUser(this);
                devGroups.add(devGroup);
            }
        }
    }

    public boolean removeGroup(DevGroup devGroup){
        if(null != devGroup) {
            devGroup.setUser(null);
            return devGroups.remove(devGroup);
        }
        return false;
    }

    public boolean removeGroup(int index){
        if(index < devGroups.size()){
            DevGroup devGroup = devGroups.get(index);
            return removeGroup(devGroup);
        }
        return false;
    }
}
