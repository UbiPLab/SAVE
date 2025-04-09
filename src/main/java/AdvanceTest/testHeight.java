package AdvanceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/7 8:52
 * @Version 1.0
 */
public class testHeight {
    public static boolean verify_Correctness_Height(List<Double> Height_list, double root_height){
        boolean flag = false;

        double[] Height = new double[Height_list.size()];
        for (int i = 0; i < Height.length; i++) {

            Height[i] = Height_list.get(i);
        }

        for (int i = 1; i < root_height ; i++) {
            System.out.println("before: "+Arrays.toString(Height));
            boolean tag = false; //  偶数变，奇数不变
            for (int j = 0; j < Height.length; j++) {
                if (Height[j] == 0){
                    continue;
                }else if (Height[j] == i){
                    if (tag){
                        Height[j] = i+1;
                        tag = false;
                    }else {
                        Height[j] = 0;
                        tag = true;
                    }
                }
            }
            System.out.println("after: "+Arrays.toString(Height));
        }
        System.out.println(Arrays.stream(Height).sum());
        if (Arrays.stream(Height).sum() == root_height){
            flag = true;
        }

        return flag;
    }

    public static boolean verify_Correctness_HV(List<Double> Height_list, double root_height, List<byte[]> HV_list, byte[] root){
        boolean flag_height = false;
        boolean flag_hv = false;

        byte[][] HV = new byte[HV_list.size()][];
        double[] Height = new double[Height_list.size()];
        for (int i = 0; i < Height.length; i++) {
            HV[i] = HV_list.get(i);
            Height[i] = Height_list.get(i);
        }

        for (int i = 1; i < root_height ; i++) {
            System.out.println("before: "+Arrays.toString(Height));
            boolean tag = false; //  偶数变，奇数不变
            byte[] temp = null;
            for (int j = 0; j < Height.length; j++) {
                if (Height[j] == 0){
                    continue;
                }else if (Height[j] == i){
                    if (tag){
                        Height[j] = i+1;
//                        HV[j] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp,HV[j]));
                        HV[j] = convertTobyte(Height[j]);
                        temp = null;
                        tag = false;
                    }else {
                        Height[j] = 0;
                        temp = HV[j].clone();
                        tag = true;
                    }
                }
            }
            System.out.println("after: "+Arrays.toString(Height));
        }
        System.out.println(Arrays.stream(Height).sum());
        if (Arrays.stream(Height).sum() == root_height){
            flag_height = true;
        }
        for (byte[] by:HV) {
            if (by != null){
                if (Samebyte(by,root)){
                    flag_hv = true;
                }else {
                    flag_hv = false;
                }
            }
        }
        if (flag_height && flag_hv)
            return true;
        else
            return false;

    }

    private static boolean Samebyte(byte[] by, byte[] root) {
        boolean flag = false;
        for (int i = 0; i < root.length; i++) {
            if (by[i] == root[i]){
                flag = true;
            }else {
                flag = false;
                return flag;
            }
        }
        return flag;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    public static byte[] convertTobyte(double v){
        Long value = Double.doubleToRawLongBits(v);
        byte[] b = new byte[8];
        for(int z = 0 ; z<8;z++){
            b[z] = (byte)((value>>8*z)&0xff);
        }
        return b;
    }

    public static void main(String[] args) {

        Double[] doubles = {4.0,1.0,1.0,2.0,1.0,1.0,2.0,3.0,3.0,4.0};
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < doubles.length; i++) {

            list.add(convertTobyte(doubles[i]));
        }
        double root_height = 6.0;
        byte[] root = convertTobyte(root_height);
        List<Double> doubleList = Arrays.asList(doubles);
//        System.out.println(verify_Correctness_Height(doubleList,6.0));
        System.out.println(verify_Correctness_HV(doubleList,root_height,list,root));
    }
}
