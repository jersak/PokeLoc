package br.com.fastertunnel.pokeloc.models;

import br.com.fastertunnel.pokeloc.R;
import br.com.fastertunnel.pokeloc.utils.ImageManager;

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

    @SerializedName("name")
    private String name;

    @SerializedName("encounterId")
    private Long encounterId;

    private boolean isWanted = false;

    public PokemonBean(String id, Long time, double latitude, double longitude, Long encounterId) {
        this.id = id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.encounterId = encounterId;
    }

    public PokemonBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {

        if (time <= 0) {
            return System.currentTimeMillis();
        } else {
            return time;
        }
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

    public Long getEncounterId() {
        return encounterId;
    }

    public BitmapDescriptor getImage() {
        return BitmapDescriptorFactory.fromResource(ImageManager.getImage(getId()));
    }

    public String getName() {
        return name;
    }

    public boolean isWanted() {
        return isWanted;
    }

    public void setWanted(boolean wanted) {
        isWanted = wanted;
    }
}
