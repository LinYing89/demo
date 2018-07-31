package com.example.demo.data.device;

/**
 * json传值使用的POJO
 */
public class ValueData {

    private String coding;
    private float value;

    public ValueData() {
    }

    public ValueData(String coding, float value) {
        this.coding = coding;
        this.value = value;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
