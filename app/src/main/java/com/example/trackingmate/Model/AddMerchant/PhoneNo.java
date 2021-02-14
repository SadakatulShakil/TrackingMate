
package com.example.trackingmate.Model.AddMerchant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhoneNo implements Serializable {

    @SerializedName("numeric")
    @Expose
    private String numeric;

    public String getNumeric() {
        return numeric;
    }

    public void setNumeric(String numeric) {
        this.numeric = numeric;
    }

    @Override
    public String toString() {
        return "PhoneNo{" +
                "numeric='" + numeric + '\'' +
                '}';
    }
}
