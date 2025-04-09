package AdvanceTest;

import Method.*;
import MyUtil.ReadFiledata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/6/14 8:56
 * @Version 1.0
 */
public class testplaintext {
    public static void main(String[] args) throws Exception {
        String[][] dataSet = ReadFiledata.readArray_String("C:\\迅雷下载\\BulletProofLib-master\\src\\main\\java\\2.txt");
//        Show.ShowMatrix(dataSet);

        System.out.println("data is ready !");
        SAVEtree etree = new SAVEtree();
        etree.Save_address = "C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SaveTemp\\";
//        String[] keylist = HashFounction.CreateSecretKey(16, 6);
//        String[] keylist = {"6992171125466298","0219126809305670","3177204288447043","8510240487633587","0632934334653356","7398280671752376"};
        String[] keylist = {"6992171125466298","0219126809305670","7398280671752376"};

        SAVETreeNode saveTree = etree.BuildSaveTree_new(dataSet,2);
        etree.Keylist = keylist.clone();
//        String[] keysingle = HashFounction.CreateSecretKey(16, 4);
        String[] keysingle = {"9348525224085664","7201477525831269","6512688678132376","8193070326567249"};

        etree.AESKey = keysingle[0];
        etree.HVKey = keysingle[1];
        etree.CubeKey = keysingle[2];
        etree.ZKPKey = keysingle[3];

        etree.initZKP(8);
        long Create_tree_time = etree.initNode_new(saveTree, new ArrayList<>());


        System.out.println("Create_tree_time = "+ Create_tree_time);

        Trapdoor trapdoor = new Trapdoor();
        trapdoor.Keylist = keylist.clone();
        trapdoor.CubeKey = keysingle[2];

        /**
         * 33.601442**-84.427262**1**0
         * 33.60225046**-84.43124156**1**0
         */
        double lat_min = 33.60;
        double lat_max = 33.61;
        double lon_min = -84.44;
        double lon_max = -84.42;
        String[][][] trapdoorGen = trapdoor.TrapdoorGen(lat_min, lat_max, lon_min, lon_max);

        System.out.println("Trapdoor is ok !");

        String[] array_str = ReadFiledata.readArray_Str(saveTree.address + "cubeCoding\\" + saveTree.flag + ".txt");
//        Show.showString_list(array_str);

        List<ProofNode> NN = new ArrayList<>();
        List<byte[]> HV_list = new ArrayList<>();
        List<Double> list_height = new ArrayList<>();
        List<Long> time_query = new ArrayList<>();
        List<Long> time_proof = new ArrayList<>();

        new Query().Query_Tree(saveTree, trapdoorGen, NN, HV_list, list_height, time_query, time_proof);

        System.out.println("NN.size()= "+ NN.size());


        System.out.println("Verify ");
        System.out.println("Verify ");
        for (ProofNode node:NN
        ) {
//            String decode = MyUtil.AESUtil.decryptByHexString(node.ciper, keysingle[0]);
//            System.out.println(node.tag);

            if (node.tag == 1){
                boolean verify_freshness = Verification.verify_Freshness(node);
                System.out.println("verify_freshness = "+verify_freshness);
                boolean verify_validity = Verification.verify_Validity(node, etree.ZKPparameters);
                System.out.println("verify_validity = "+verify_validity);
            }

        }
        String user_input = lat_min+"**"+lat_max+"**"+lon_min+"**"+lon_max;

        boolean correctness_decrypt = Verification.verify_Correctness_Decrypt(NN, user_input, keysingle[0]);
        System.out.println("correctness_decrypt = "+correctness_decrypt);


        boolean verify_correctness_hv = Verification.verify_Correctness_HV(list_height, saveTree.height, HV_list, saveTree.HV);
        System.out.println("verify_correctness_hv = "+verify_correctness_hv);
//        boolean verify_completeness = Verification.verify_Completeness(NN, trapdoorGen, keylist);
//        System.out.println("verify_completeness = "+verify_completeness);

    }

    public static void insert(byte[][] twinlist, String data ,String[] Keylist , int rb) throws Exception {
        for (int i = 0; i < Keylist.length - 1; i++) {
            byte[] outbytes = MyUtil.HashFounction.HmacSHA256Encrypt(data,Keylist[i]);    //  HMAC(w,k_i)
            BigInteger bi = new BigInteger(1, outbytes);
            int twinindex = bi.mod(BigInteger.valueOf(twinlist[0].length)).intValue();      //  twins_id
            //now we get k twins

            //for each twin, compute the chosen location
            byte[] hkp1 = MyUtil.HashFounction.mdinstance.digest(addBytes(outbytes, Keylist[Keylist.length - 1].getBytes()));//h_k+1
            BigInteger hkp1bi = new BigInteger(1, hkp1);
            byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(rb)).toByteArray());  //sha1_xor rb
            int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

            if (location == 0) {
                twinlist[0][twinindex] = 1;
                twinlist[1][twinindex] = 0;
            } else {
                twinlist[1][twinindex] = 1;
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
