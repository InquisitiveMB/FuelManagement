package Modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Route {
    public Distance distance;
    public Duration duration;

    public String endAddress;
    public String startAddress;

    public LatLng endLocation;
    public LatLng startLocation;

    public List<LatLng> points;
}
