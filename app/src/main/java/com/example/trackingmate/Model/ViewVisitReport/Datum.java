package com.example.trackingmate.Model.ViewVisitReport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("market_name")
    @Expose
    private String marketName;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("shop_address")
    @Expose
    private String shopAddress;
    @SerializedName("business_type")
    @Expose
    private String businessType;
    @SerializedName("products_type")
    @Expose
    private String productsType;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("owner_phone")
    @Expose
    private String ownerPhone;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getProductsType() {
        return productsType;
    }

    public void setProductsType(String productsType) {
        this.productsType = productsType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "id='" + id + '\'' +
                ", marketName='" + marketName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", businessType='" + businessType + '\'' +
                ", productsType='" + productsType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }
}
