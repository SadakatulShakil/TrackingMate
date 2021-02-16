
package com.futureskyltd.trackingmate.Model.AddMerchant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Email implements Serializable {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique")
    @Expose
    private String unique;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                ", unique='" + unique + '\'' +
                '}';
    }
}
