package com.futureskyltd.trackingmate.Model.ViewVisitReport;


import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewVisitReport implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ViewVisitReport{" +
                "data=" + data +
                '}';
    }
}
