package Method;

import BasicTechnique.CubeEncoding;
import MyUtil.DataConvert;
import MyUtil.Show;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/5 11:24
 * @Version 1.0
 */
public class Trapdoor {

    public String[] Keylist;
    public String CubeKey;

    public String[][][] TrapdoorGen( double lat_min, double lat_max,double lon_min, double lon_max) throws Exception {
        String[] cube_codes = new CubeEncoding().LevelElement_Query(CubeKey, lon_min, lon_max, lat_min, lat_max);
        System.out.println("Trapdoor:");
//        Show.showString_list(cube_codes);

        String[][][] TD = new String[cube_codes.length][Keylist.length - 1][2];
        for (int i = 0; i < cube_codes.length; i++) {
            for (int j = 0; j < Keylist.length - 1; j++) {
                byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(cube_codes[i], Keylist[j]);    //  HMAC(w,k_i)
                TD[i][j][0] = DataConvert.toHexString(outbytes);
                byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
                TD[i][j][1] = DataConvert.toHexString(hkp1);
            }
        }
        return TD;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }
}
