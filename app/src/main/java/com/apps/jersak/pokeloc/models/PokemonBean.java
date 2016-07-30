package com.apps.jersak.pokeloc.models;

import com.apps.jersak.pokeloc.utils.ImageManager;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fuzi on 30/07/2016.
 */
public class PokemonBean {

    @SerializedName("id")
    private String id;

    @SerializedName("time")
    private Long time;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public PokemonBean(String id, Long time, double latitude, double longitude) {
        this.id = id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public BitmapDescriptor getImage(){
        return BitmapDescriptorFactory.fromResource(ImageManager.getImage(getId()));
    }

}
