package BasicTechnique;

import MyUtil.DataConvert;
import MyUtil.HashFounction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/11 10:38
 * @Version 1.0
 */
public class CubeEncoding {
    public final static int MAX_LEVEL_NUM = 3;
    public final static int bitsize = 8;
    public final static double LAT_MIN = 30.0027356468;
    public final static double LAT_MAX = 30.44998;
    public final static double LON_MIN = -98.0827756;
    public final static double LON_MAX = -97.5559387;
    public final static int TIME_MIN = 0;
    public final static int TIME_MAX = 47;



    public String[] LevelElement(String sk,Data_Point data_point) throws Exception {
        int level_id = 1;
        ArrayList<String> arrayList = new ArrayList<>();
        int total_element_num = 0;
        while (level_id < MAX_LEVEL_NUM){
            double level_split = Math.pow(2.0, level_id);
            double projection_lat = Projection(LAT_MIN, LAT_MAX, data_point.lat, level_split);
            double projection_lon = Projection(LON_MIN, LON_MAX, data_point.lon, level_split);
//            System.out.println(level_id+","+projection_lat+","+projection_lon);
            byte[] bytes = HashFounction.HmacSHA256Encrypt(String.valueOf((level_id + projection_lat + projection_lon)), sk);
            arrayList.add(DataConvert.toHexString(bytes));
            total_element_num++;
            level_id++;
        }
        if (level_id == MAX_LEVEL_NUM){
            double level_split = Math.pow(2.0, level_id);
            double projection_lat = Projection(LAT_MIN, LAT_MAX, data_point.lat, level_split);
            double projection_lon = Projection(LON_MIN, LON_MAX, data_point.lon, level_split);
            String[] F_lat = PrefixEncoding.Prefix(bitsize, (int) projection_lat);
            String[] F_lon = PrefixEncoding.Prefix(bitsize, (int) projection_lon);
            total_element_num =total_element_num + F_lat.length+ F_lon.length;
            for (String s:F_lat) {
//                System.out.println(level_id+s);
                arrayList.add(DataConvert.toHexString(HashFounction.HmacSHA256Encrypt(String.valueOf(level_id)+s,sk)));
            }
            for (String s:F_lon) {
//                System.out.println(level_id+s);
                arrayList.add(DataConvert.toHexString(HashFounction.HmacSHA256Encrypt(String.valueOf(level_id)+s,sk)));
            }
        }
        return arrayList.toArray(new String[total_element_num]);
    }

    public String[] LevelElement_Query(String sk, double lon_min, double lon_max, double lat_min, double lat_max ) throws Exception {
        int level_id = 1;
        ArrayList<String> arrayList = new ArrayList<>();
        int total_element_num = 0;
        while (level_id < MAX_LEVEL_NUM){
            double level_split = Math.pow(2.0, level_id);
            level_id++;
            double projection_lat_min = Projection(LAT_MIN, LAT_MAX, lat_min, level_split);
            double projection_lat_max = Projection(LAT_MIN, LAT_MAX, lat_max, level_split);
//            if (projection_lat_min != projection_lat_max)
//                continue;
            double projection_lon_min = Projection(LON_MIN, LON_MAX, lon_min, level_split);
            double projection_lon_max = Projection(LON_MIN, LON_MAX, lon_max, level_split);
//            if (projection_lon_min != projection_lon_max)
//                continue;
//            System.out.println(level_id+","+projection_lat+","+projection_lon);
            byte[] bytes = HashFounction.HmacSHA256Encrypt(String.valueOf((level_id + projection_lat_min + projection_lon_min)), sk);
            arrayList.add(DataConvert.toHexString(bytes));
            total_element_num++;

        }
        System.out.println("last level");
        if (level_id == MAX_LEVEL_NUM){
            double level_split = Math.pow(2.0, level_id);
            double projection_lat_min = Projection(LAT_MIN, LAT_MAX, lat_min, level_split);
            double projection_lat_max = Projection(LAT_MIN, LAT_MAX, lat_max, level_split);
            double projection_lon_min = Projection(LON_MIN, LON_MAX, lon_min, level_split);
            double projection_lon_max = Projection(LON_MIN, LON_MAX, lon_max, level_split);
            String[] F_lat = PrefixEncoding.range(bitsize, (int) projection_lat_min, (int) projection_lat_max);
            String[] F_lon = PrefixEncoding.range(bitsize, (int) projection_lon_min, (int) projection_lon_max);
            total_element_num =total_element_num + F_lat.length+ F_lon.length;
            for (String s:F_lat) {
//                System.out.println(level_id+s);
                arrayList.add(DataConvert.toHexString(HashFounction.HmacSHA256Encrypt(String.valueOf(level_id)+s,sk)));
            }
            for (String s:F_lon) {
//                System.out.println(level_id+s);
                arrayList.add(DataConvert.toHexString(HashFounction.HmacSHA256Encrypt(String.valueOf(level_id)+s,sk)));
            }
        }
        return arrayList.toArray(new String[total_element_num]);
    }

    public String[] LevelElement_Complementary(String sk,Data_Point data_point) throws Exception {
        int level_id = 1;
        ArrayList<String> arrayList = new ArrayList<>();
        int total_element_num = 0;
        while (level_id < MAX_LEVEL_NUM+1){
            double level_split = Math.pow(2.0, level_id);
            double projection_lat = Projection(LAT_MIN, LAT_MAX, data_point.lat, level_split);
            double projection_lon = Projection(LON_MIN, LON_MAX, data_point.lon, level_split);
            String[] otherGrid = otherGrid(level_id, level_split, projection_lat, projection_lon);
//            System.out.println("otherGrid: "+otherGrid.length);
            for (String s:otherGrid) {
                byte[] bytes = HashFounction.HmacSHA256Encrypt(s, sk);
                arrayList.add(DataConvert.toHexString(bytes));
                total_element_num++;
            }

            level_id++;
        }
        return arrayList.toArray(new String[total_element_num]);
    }

    public String[] LevelElement_Complementary(String sk,Data_Point[] data_point) throws Exception {
        int level_id = 1;
        ArrayList<String> arrayList = new ArrayList<>();
        int total_element_num = 0;
        while (level_id < MAX_LEVEL_NUM){
            List<String> each_level_com = new ArrayList<>();
            double level_split = Math.pow(2.0, level_id);
//            List<Integer> list_SetGridID = new ArrayList<>();
//            for (Data_Point dataPoint : data_point) {
//                double projection_lat = Projection(LAT_MIN, LAT_MAX, dataPoint.lat, level_split);
//                double projection_lon = Projection(LON_MIN, LON_MAX, dataPoint.lon, level_split);
//                dataPoint.gridID[level_id] = ComputeGridID(level_split, projection_lat, projection_lon);
//                list_SetGridID.add(dataPoint.gridID[level_id]);
//            }
//            int[] ids = list_SetGridID.stream().distinct().mapToInt(Integer::intValue).toArray();
            for (Data_Point point:data_point) {
                double projection_lat = Projection(LAT_MIN, LAT_MAX, point.lat, level_split);
                double projection_lon = Projection(LON_MIN, LON_MAX, point.lon, level_split);
                String[] otherGrid = otherGrid(level_id, level_split, projection_lat, projection_lon);
                each_level_com.addAll(Arrays.asList(otherGrid));
            }
            each_level_com = each_level_com.stream().distinct().collect(Collectors.toList());

            for (String s:each_level_com) {
                byte[] bytes = HashFounction.HmacSHA256Encrypt(s, sk);
                arrayList.add(DataConvert.toHexString(bytes));
                total_element_num++;
            }
            level_id++;
        }
        return arrayList.toArray(new String[total_element_num]);
    }

    /**
     * 从n*n中去除(value_x,value_y)，剩余数值组成数组
     *
     */
    private String[] otherGrid(int level_id,double n, double value_x, double value_y) {
        String[] ints = new String[(int) (Math.pow(n,2.0)-1)];
        int time = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i!=value_x || j!= value_y) {
                    ints[time] = String.valueOf(level_id + i + j);
//                    System.out.println(level_id+","+i+","+j);
                    time++;
                }
            }
        }
        return ints;
    }


    public double Projection(double min, double max, double value, double grid_num){
        double result = (value - min) / (max - min) * grid_num;
        return Math.floor(result);
    }

    public int ComputeGridID(double grid_eachLine, double lat, double lon){
        double result = lon * grid_eachLine + lat;
        return (int) result;
    }


}
