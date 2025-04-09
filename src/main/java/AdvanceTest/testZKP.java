package AdvanceTest;

import BasicTechnique.ZKP;
import MyUtil.ReadFiledata;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/15 14:45
 * @Version 1.0
 */
public class testZKP {
    public static void main(String[] args) throws Exception {
        BigInteger[] parameter = new BigInteger[]{BigInteger.valueOf(1019), BigInteger.valueOf(2039), BigInteger.valueOf(4079), BigInteger.valueOf(379)};

        System.out.println("r = "+parameter[0]);
        System.out.println("q = "+parameter[1]);
        System.out.println("p = "+parameter[2]);
        System.out.println("g = "+parameter[3]);


        byte[][] twinlist = ReadFiledata.readArray_byte("C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SaveTemp\\ibf1\\0.txt");
//        String ibf = new BigInteger(twinlist[0]).toString()+"|"+ new BigInteger(twinlist[1]).toString();
        String ibf = new String(twinlist[0])+"|"+ new String(twinlist[1]);

        BigInteger[] compute = ZKP.compute(parameter[0], parameter[1], parameter[2], parameter[3], 1,ibf,"sk2");


        BigInteger A = compute[0];
        BigInteger B = compute[1];
        BigInteger u1 = compute[2];
        BigInteger u2 = compute[3];
        BigInteger v1 = compute[4];
        BigInteger v2 = compute[5];
        BigInteger f = compute[6];
        BigInteger F = compute[7];
        BigInteger rc = compute[8];
        BigInteger rc_2 = v1.add(v2).mod(parameter[1]);

//        BigInteger f_u1 = B.multiply((F.divide(parameter[3])).modPow(v1,parameter[2])).mod(parameter[2]);
        BigInteger f_u1 = B.multiply((F.multiply(parameter[3].modInverse(parameter[2]))).modPow(v1,parameter[2])).mod(parameter[2]);

        BigInteger f_u2 = A.multiply(F.modPow(v2,parameter[2])).mod(parameter[2]);
        BigInteger f_o = f.modPow(u1, parameter[2]);
        BigInteger f_k = f.modPow(u2, parameter[2]);
        boolean verify;
        if (rc.compareTo(rc_2) == 0){
            if (f_o.compareTo(f_u1) == 0){
                if (f_k.compareTo(f_u2) == 0){
                    System.out.println("true");
                }else {
                    System.out.println("false1_f_u2");
                }
            }else {
                System.out.println("false2_f_u1");
            }
        }else {
            System.out.println("false3_rc");
        }

        System.out.println(rc+","+rc_2);
    }
}
