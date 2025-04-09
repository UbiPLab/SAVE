package AdvanceTest;

import MyUtil.ReadFiledata;
import MyUtil.Show;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/5/18 11:23
 * @Version 1.0
 */
public class doDataSet {
    public static void main(String[] args) {
        String[][] dataSet = ReadFiledata.readArray_String("C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SAVE DataSet\\24429.txt");
//        Show.ShowMatrix(dataSet);
        int length = 20000;
        Random random = new Random();
        boolean[] bool = new boolean[length];
        int[] result = new int[length];
        int rand = 0;
        for (int j = 0; j < length; j++) {
            /*
             * 得到 length 个不同的随机数
             */
            do {
                rand = random.nextInt(length);
                result[j] = rand;
            } while (bool[rand]);
            bool[rand] = true;
        }
        // [3, 1, 2, 4, 0]    第一次
        // [3, 1, 4, 0, 2]    第二次
//        System.out.println(Arrays.toString(result));

        int aim = 10000;

        String[][] data = new String[aim][];
        for (int i = 0; i < aim; i++) {
            data[i] = dataSet[result[i]];
        }
        String address = "C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SAVE DataSet\\"+String.valueOf(aim)+".txt";
        ReadFiledata.saveArray(data,address);
    }



}
