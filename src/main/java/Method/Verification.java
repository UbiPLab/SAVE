package Method;

import BasicTechnique.Bulletproof;
import MyUtil.HashFounction;
import MyUtil.ReadFiledata;
import MyUtil.Show;
import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.VerificationFailedException;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Point;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProof;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/6 10:54
 * @Version 1.0
 */
public class Verification {
//    public static String HVKey;

    public static boolean verify_Validity(ProofNode leaf, BigInteger[] ZKPparameters){
        BigInteger r = ZKPparameters[0];
        BigInteger q = ZKPparameters[1];
        BigInteger p = ZKPparameters[2];
        BigInteger g = ZKPparameters[3];
        boolean ver = false;

            if (leaf.tag == 1){
                BigInteger A = leaf.ZKP[0];
                BigInteger B = leaf.ZKP[1];
                BigInteger u1 = leaf.ZKP[2];
                BigInteger u2 = leaf.ZKP[3];
                BigInteger v1 = leaf.ZKP[4];
                BigInteger v2 = leaf.ZKP[5];
                BigInteger f = leaf.ZKP[6];
                BigInteger F = leaf.ZKP[7];
                BigInteger rc = leaf.ZKP[8];

                BigInteger rc_2 = (v1.add(v2)).mod(q);
//                BigInteger f_u1 = B.multiply((F.multiply(g.modInverse(q))).modPow(v1,q)).mod(p);
//                BigInteger f_u2 = A.multiply(F.modPow(v2,q)).mod(p);
//                BigInteger f_o = f.modPow(u1, q);
//                BigInteger f_k = f.modPow(u2, q);
                BigInteger f_u1 = B.multiply((F.multiply(g.modInverse(p))).modPow(v1,p)).mod(p);
                BigInteger f_u2 = A.multiply(F.modPow(v2,p)).mod(p);
                BigInteger f_o = f.modPow(u1, p);
                BigInteger f_k = f.modPow(u2, p);
                if (rc.compareTo(rc_2) == 0){
                    if (f_o.compareTo(f_u1) == 0){
                        if (f_k.compareTo(f_u2) == 0){
                            ver = true;
                        }else {
//                            System.out.println("false1_f_k");
                             ver = false;

                        }
                    }else {
//                        System.out.println("false2_f_o");
                        ver = false;
                    }
                }else {
//                    System.out.println("false3_rc");
                    ver = false;
                }
            }


        return ver;
    }

    public static boolean verify_Freshness(ProofNode leaf) throws VerificationFailedException {

            if (leaf.tag == 1) {
                boolean proof = Bulletproof.verifyProof((GeneratorParams<BN128Point>)leaf.Bulletproof[0], (BN128Point)leaf.Bulletproof[1], (RangeProof<BN128Point>)leaf.Bulletproof[2]);
                return proof;
            }

        return false;
    }

    public static boolean verify_Completeness(List<ProofNode> p_tree,  String[][][] TD,  String[] Keylist, String HVKey,List<Long> list_time_read) throws Exception {
        boolean flag = true;

        for (ProofNode node:p_tree) {
            if (node.tag != 1){
                long start_read = System.currentTimeMillis();
                //  读取节点twinlist
                byte[][] twinlist = ReadFiledata.readArray_byte(node.address+"ibf2\\"+node.flag+".txt");
//                Show.ShowMatrix(twinlist);
                long end_read = System.currentTimeMillis();
                list_time_read.add((end_read-start_read));

                for (int i = 0; i < TD.length; i++) {
                    //  每一行都符合
                    for (int j = 0; j < Keylist.length-1; j++) {
                        //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分

                        byte[] outbytes = toByteArray(TD[i][j][0]);
                        BigInteger bi = new BigInteger(1, outbytes);
                        int twinindex = bi.mod(BigInteger.valueOf(node.ibf_length)).intValue();      //  twins_id

                        byte[] hkp1 = toByteArray(TD[i][j][1]);
                        BigInteger hkp1bi = new BigInteger(1, hkp1);
                        byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(node.rb)).toByteArray());  //sha1_xor rb
                        int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                        int locate = twinindex / node.seg;
                        //  check hv_seg
                        String cut = new BigInteger(Arrays.copyOfRange(twinlist[0], 0, node.seg)).toString()+"|"+new BigInteger(Arrays.copyOfRange(twinlist[1], 0, node.seg)).toString();
                        byte[] cut_hv = HashFounction.HmacSHA256Encrypt(cut,HVKey);
//                        if (!Samebyte(cut_hv,node.hv[0])){
//                            return false;
//                        }
                        if (locate == 0){
                            if (location == 0){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }


    public static boolean verify_Correctness_Decrypt(List<ProofNode> C , String user_input, String AESKey){
        boolean flag = false;
        int right_count = 0;
        int total_count = 0;
        String[] strs_user = user_input.split("\\*\\*");
        //  "lat_min**lat_max**lon_min**lon_max
        double lat_min = Double.parseDouble(strs_user[0]);
        double lat_max = Double.parseDouble(strs_user[1]);
        double lon_min = Double.parseDouble(strs_user[2]);
        double lon_max = Double.parseDouble(strs_user[3]);

        for (ProofNode leaf:C) {
            if (leaf.tag == 1) {
                total_count++;
                //  lat | lon | time | 0/1
                String decode = MyUtil.AESUtil.decryptByHexString(leaf.ciper, AESKey);
//                System.out.println("decode : "+decode);
                String[] result = decode.split("\\|");
                double lat = Double.parseDouble(result[0]);
                double lon = Double.parseDouble(result[1]);
//                int time = Integer.parseInt(result[2]);
                //  判断
                if ( (lat >= lat_min) && (lat <= lat_max) && (lon >= lon_min) && (lon <= lon_max)){
                    System.out.println(lat+","+lon);
                    right_count++;
                } else {
                    System.out.println("存在一个假元素：" + decode);
//                    System.out.println(user_input);
//                    System.out.println(decode);
                }
            }
        }
        if (right_count == total_count){
            flag = true;
        }else {
            flag = false;
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
//            System.out.println("before: "+Arrays.toString(Height));
            boolean tag = false; //  偶数变，奇数不变
            byte[] temp = null;
            for (int j = 0; j < Height.length; j++) {
                if (Height[j] == 0){
                    continue;
                }else if (Height[j] == i){
                    if (tag){
                        Height[j] = i+1;
                        HV[j] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp,HV[j]));
//                        HV[j] = convertTobyte(Height[j]);
                        temp = null;
                        tag = false;
                    }else {
                        Height[j] = 0;
                        temp = HV[j].clone();
                        tag = true;
                    }
                }
            }
//            System.out.println("after: "+Arrays.toString(Height));
        }
        System.out.println(Arrays.stream(Height).sum());
        if (Arrays.stream(Height).sum() == root_height){
            flag_height = true;
        }
        for (byte[] by:HV) {
            if (by != null){
                flag_hv = Samebyte(by,root);
            }
        }
        if (flag_height && flag_hv)
//        if (flag_hv)
            return true;
        else
            return false;

    }

    public static boolean verify_Correctness_HV_full(List<Double> Height_list, double root_height, List<byte[]> HV_list, byte[] root){
        boolean flag_height = false;
        boolean flag_hv = false;

        byte[][] HV = new byte[HV_list.size()][];
        double[] Height = new double[Height_list.size()];
        for (int i = 0; i < Height.length; i++) {
            HV[i] = HV_list.get(i);
            Height[i] = Height_list.get(i);
        }

        for (int i = 1; i < root_height ; i++) {
//            System.out.println("before: "+Arrays.toString(Height));
            boolean tag = false; //  偶数变，奇数不变
            byte[] temp = null;
            for (int j = 0; j < Height.length; j++) {
                if (Height[j] == 0){
                    continue;
                }else if (Height[j] == i){
                    if (tag){
                        Height[j] = i+1;
                        HV[j] = MyUtil.HashFounction.mdinstance.digest(addBytes(temp,HV[j]));
//                        HV[j] = convertTobyte(Height[j]);
                        temp = null;
                        tag = false;
                    }else {
                        Height[j] = 0;
                        temp = HV[j].clone();
                        tag = true;
                    }
                }
            }
//            System.out.println("after: "+Arrays.toString(Height));
        }
//        System.out.println(Arrays.stream(Height).sum());
        if (Arrays.stream(Height).sum() == root_height){
            flag_height = true;
        }
        for (byte[] by:HV) {
            if (by != null){
                flag_hv = Samebyte(by,root);
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

    public static byte[] convertTobyte(double v){
        Long value = Double.doubleToRawLongBits(v);
        byte[] b = new byte[8];
        for(int z = 0 ; z<8;z++){
            b[z] = (byte)((value>>8*z)&0xff);
        }
        return b;
    }


    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    public static byte[] addBytes(Byte[] data1, Byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        for (int i = 0; i < data1.length; i++) {
            data3[i] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            data3[i+data1.length] = data2[i];
        }
        return data3;
    }


    public static boolean SameElement(String[] ycs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < ycs.length; i++) {
            if (list.contains(ycs[i])){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv(byte[][] ycs_Hv,List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < ycs_Hv.length; i++) {
            if (list.contains(ycs_Hv[i])){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean SameElement_2(String[][] lcs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < lcs.length; i++) {
            if (list.contains(lcs[i])){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv_2(byte[][][] lcs_Hv,List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < lcs_Hv.length; i++) {
            for (int j = 0; j < lcs_Hv[i].length; j++) {
                if (list.contains(lcs_Hv[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement(String[][] ycs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < ycs.length; i++) {
            for (int j = 0; j < ycs[i].length; j++) {
                if (list.contains(ycs[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv(byte[][][] ycs_Hv, List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < ycs_Hv.length; i++) {
            for (int j = 0; j < ycs_Hv[i].length; j++) {
                if (list.contains(ycs_Hv[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_2(String[][][] lcs,List<String> list){
        boolean flag = false;
        for (int i = 0; i < lcs.length; i++) {
            for (int j = 0; j < lcs[i].length; j++) {
                if (list.contains(lcs[i][j])){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean SameElement_Hv_2(byte[][][][] lcs_Hv,List<byte[]> list){
        boolean flag = false;
        for (int i = 0; i < lcs_Hv.length; i++) {
            for (int j = 0; j < lcs_Hv[i].length; j++) {
                for (int k = 0; k < lcs_Hv[i][j].length; k++) {
                    if (list.contains(lcs_Hv[i][j][k])){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }
}
