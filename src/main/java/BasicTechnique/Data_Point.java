package BasicTechnique;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/11 10:44
 * @Version 1.0
 */
public class Data_Point {
    public double lat;
    public double lon;
    public int[] gridID = new int[3];

    public Data_Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }


}
