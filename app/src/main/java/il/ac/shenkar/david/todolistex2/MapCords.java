package il.ac.shenkar.david.todolistex2;

import java.io.Serializable;

/**
 * Created by David on 05-Dec-15.
 */
public class MapCords implements Serializable
{
    private double latitude;
    private double longitute;

    public MapCords (double lat,double lng)
    {
        this.latitude = lat;
        this.longitute = lng;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lat) {
        this.latitude = lat;
    }

    public double getLng() {
        return longitute;
    }

    public void setLng(double lng) {
        this.longitute = lng;
    }
}
