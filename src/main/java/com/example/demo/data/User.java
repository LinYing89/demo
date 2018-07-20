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
    private List<Group> groups = new ArrayList<>();

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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group){
        if(null != group){
            if(!groups.contains(group)){
                group.setUser(this);
                groups.add(group);
            }
        }
    }

    public boolean removeGroup(Group group){
        if(null != group) {
            group.setUser(null);
            return groups.remove(group);
        }
        return false;
    }

    public boolean removeGroup(int index){
        if(index < groups.size()){
            Group group = groups.get(index);
            return removeGroup(group);
        }
        return false;
    }
}
