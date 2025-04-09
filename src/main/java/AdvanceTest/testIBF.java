package AdvanceTest;

import BasicTechnique.IBF;
import MyUtil.HashFounction;
import MyUtil.Show;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/13 9:03
 * @Version 1.0
 */
public class testIBF {
    public static void main(String[] args) throws Exception {
        String[] key = HashFounction.CreateSecretKey(4);
        IBF ibf = new IBF(key, 10, 10);
        ibf.insert("sji");
        Show.ShowMatrix(ibf.twinlist);
        System.out.println(ibf.twinlist.toString());

    }
}
