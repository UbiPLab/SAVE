package AdvanceTest;

import BasicTechnique.CubeEncoding;
import MyUtil.Show;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/5/9 11:31
 * @Version 1.0
 */
public class testD {
    public static void main(String[] args) throws Exception {
        int num = 2;
        int height = (int) ((Math.ceil(Math.log(num) / Math.log(2))) + 1);
        System.out.println(height);
        int num_left = (int) Math.pow(2,height-2);
        System.out.println(num_left);
    }

}
