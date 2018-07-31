package com.example.demo.data.device;

import com.example.demo.data.Untils;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 电量信息, 电压\电流\功率
 * @author 44489
 *
 */
@Entity
public class EleInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    //A相电流
    private float axA;
    //B相电流
    private float bxA;
    //C相电流
    private float cxA;
    //A相电压
    private float axV;
    //B相电压
    private float bxV;
    //C相电压
    private float cxV;
    //功率因数
    private float yinshu;

    @OneToOne
    @JsonBackReference("ele_info")
    private Electrical electrical;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public float getAxA() {
        return axA;
    }
    public void setAxA(float axA) {
        this.axA = axA;
    }
    public float getBxA() {
        return bxA;
    }
    public void setBxA(float bxA) {
        this.bxA = bxA;
    }
    public float getCxA() {
        return cxA;
    }
    public void setCxA(float cxA) {
        this.cxA = cxA;
    }
    public float getAxV() {
        return axV;
    }
    public void setAxV(float axV) {
        this.axV = axV;
    }
    public float getBxV() {
        return bxV;
    }
    public void setBxV(float bxV) {
        this.bxV = bxV;
    }
    public float getCxV() {
        return cxV;
    }
    public void setCxV(float cxV) {
        this.cxV = cxV;
    }
    public float getYinshu() {
        return yinshu;
    }
    public void setYinshu(float yinshu) {
        this.yinshu = yinshu;
    }

    /**
     * A相有功功率
     * @return A相有功功率
     */
    public float getAxyg(){
        return Untils.scale(axA * axV * yinshu / 1000);
    }

    /**
     * A相无功功率
     * @return A相无功功率
     */
    public float getAxwg(){
        return Untils.scale(axA * axV * (1 - yinshu) / 1000);
    }

    /**
     * B相有功功率
     * @return B相有功功率
     */
    public float getBxyg(){
        return Untils.scale(bxA * bxV * yinshu / 1000);
    }

    /**
     * B相无功功率
     * @return B相无功功率
     */
    public float getBxwg(){
        return Untils.scale(bxA * bxV * (1 - yinshu) / 1000);
    }

    /**
     * C相有功功率
     * @return C相有功功率
     */
    public float getCxyg(){
        return Untils.scale(cxA * cxV * yinshu / 1000);
    }

    /**
     * C相无功功率
     * @return C相无功功率
     */
    public float getCxwg(){
        return Untils.scale(cxA * cxV * (1 - yinshu) / 1000);
    }

    public Electrical getElectrical() {
        return electrical;
    }

    public void setElectrical(Electrical electrical) {
        this.electrical = electrical;
    }

    /**
     * 总有功功率
     * @return 总有功功率
     */
    public float zongYouGongPower() {
        return Untils.scale(getAxyg() + getBxyg() + getCxyg());
    }

    /**
     * 总相无功功率
     * @return 总相无功功率
     */
    public float zongWuGongPower() {
        return Untils.scale(getAxwg() + getBxwg() + getCxwg());
    }


}