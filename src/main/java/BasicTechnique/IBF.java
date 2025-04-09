package BasicTechnique;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/13 8:47
 * @Version 1.0
 */
public class IBF {
    public String[] keylist;
    public int ibf_length;
    public byte[][] twinlist;
    public int ibf_randomNumber;

    public IBF(String[] keylist, int ibf_length, int ibf_randomNumber) {
        this.keylist = keylist;
        this.ibf_length = ibf_length;
        this.ibf_randomNumber = ibf_randomNumber;
        twinlist = new byte[2][ibf_length];
    }


    public String[] getKeylist() {
        return keylist;
    }

    public void setKeylist(String[] keylist) {
        this.keylist = keylist;
    }

    public int getIbf_length() {
        return ibf_length;
    }

    public void setIbf_length(int ibf_length) {
        this.ibf_length = ibf_length;
    }

    public byte[][] getTwinlist() {
        return twinlist;
    }

    public void setTwinlist(byte[][] twinlist) {
        this.twinlist = twinlist;
    }

    public int getIbf_randomNumber() {
        return ibf_randomNumber;
    }

    public void setIbf_randomNumber(int ibf_randomNumber) {
        this.ibf_randomNumber = ibf_randomNumber;
    }


    public String computeHash(){
        return new BigInteger(twinlist[0]).toString()+"|"+ new BigInteger(twinlist[1]).toString();
    }

    public String twinslist_toString(){
        return new String(twinlist[0])+"|"+ new String(twinlist[1]);
    }

    public String[] computeSegment(int seg_number,int each_seg_length){
        String[] s = new String[seg_number];
        int start_index = 0;
        int end_index = 0;
        for (int i = 0; i < seg_number-1; i++) {
            start_index = i*each_seg_length;
            end_index = (i+1)*each_seg_length;
            s[i] = new BigInteger(Arrays.copyOfRange(twinlist[0], start_index, end_index)).toString()+"|"+new BigInteger(Arrays.copyOfRange(twinlist[1], start_index, end_index)).toString();
        }
        s[seg_number-1] = new BigInteger(Arrays.copyOfRange(twinlist[0], end_index,ibf_length)).toString()+"|"+new BigInteger(Arrays.copyOfRange(twinlist[1], end_index,ibf_length)).toString();
        return s;
    }

    /**
     * 将数据插入ibf
     * @param data_item

     * @throws Exception
     */
    public  void insert(String data_item) throws Exception {
        for (int i = 0; i < keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data_item,keylist[i]);    //  HMAC(w,k_i)
            BigInteger bi = new BigInteger(1, outbytes);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist[0].length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
//            byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
            byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(Arrays.toString(outbytes),keylist[keylist.length-1]);//h_k+1
            BigInteger hkp1bi = new BigInteger(1, hkp1);
            byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(ibf_randomNumber)).toByteArray());  //sha1_xor rb
            int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

//            System.out.println(twinindex+" , "+location);
            if (location == 0) {
                twinlist[0][twinindex] = 1;
                twinlist[1][twinindex] = 0;
            } else {
                twinlist[1][twinindex]= 1;
                twinlist[0][twinindex] = 0;
            }
        }
    }


    /**
     * 将数据插入twinlist
     * @param twinlist
     * @param data_item
     * @param Keylist
     * @param rb
     * @throws Exception
     */
    public static void insert(byte[][] twinlist, String data_item ,String[] Keylist , int rb) throws Exception {
        for (int i = 0; i < Keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data_item,Keylist[i]);    //  HMAC(w,k_i)
            BigInteger bi = new BigInteger(1, outbytes);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist[0].length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
//            byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
            byte[] hkp1 = MyUtil.HashFounction.HmacSHA256Encrypt(Arrays.toString(outbytes),Keylist[Keylist.length-1]);//h_k+1
            BigInteger hkp1bi = new BigInteger(1, hkp1);
            byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
            int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

//            System.out.println(twinindex+" , "+location);
            if (location == 0) {
                twinlist[0][twinindex] = 1;
                twinlist[1][twinindex] = 0;
            } else {
                twinlist[1][twinindex]= 1;
                twinlist[0][twinindex] = 0;
            }
        }
    }



    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

}
