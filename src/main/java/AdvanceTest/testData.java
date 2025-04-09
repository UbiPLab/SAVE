package AdvanceTest;

import Method.*;
import MyUtil.ReadFiledata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/5/5 11:16
 * @Version 1.0
 */
public class testData {
    public static void main(String[] args) throws Exception {
        String[][] dataSet = ReadFiledata.readArray_String("C:\\迅雷下载\\BulletProofLib-master\\src\\main\\java\\20.txt");
//        Show.ShowMatrix(dataSet);

        System.out.println("data is ready !");

        long start_tree = System.currentTimeMillis();

        SAVEtree etree = new SAVEtree();
        etree.Save_address = "C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SaveTemp\\";
//        String[] keylist = HashFounction.CreateSecretKey(16, 6);
//        String[] keylist = {"6992171125466298","0219126809305670","3177204288447043","8510240487633587","0632934334653356","7398280671752376"};
        String[] keylist = {"6992171125466298","0219126809305670","7398280671752376"};

        int height = (int) ((Math.ceil(Math.log(dataSet.length) / Math.log(2))) + 1);
        SAVETreeNode saveTree = etree.BuildSaveTree_new(dataSet,height);
        etree.Keylist = keylist.clone();
//        String[] keysingle = HashFounction.CreateSecretKey(16, 4);
        String[] keysingle = {"9348525224085664","7201477525831269","6512688678132376","8193070326567249"};

        etree.AESKey = keysingle[0];
        etree.HVKey = keysingle[1];
        etree.CubeKey = keysingle[2];
        etree.ZKPKey = keysingle[3];

        etree.initZKP(8);
        long Create_tree_time = etree.initNode_new(saveTree, new ArrayList<>());
        long end_tree = System.currentTimeMillis();



        System.out.println("Create_tree_time = "+ Create_tree_time +" ms");
        System.out.println("Create_tree_time_2 = "+ (end_tree-start_tree) +" ms");

        long start_trapdoor = System.currentTimeMillis();
        Trapdoor trapdoor = new Trapdoor();
        trapdoor.Keylist = keylist.clone();
        trapdoor.CubeKey = keysingle[2];
        double lat_min = 30.21;
        double lat_max = 30.25;
        double lon_min = -97.79;
        double lon_max = -97.71;
        String[][][] trapdoorGen = trapdoor.TrapdoorGen(lat_min, lat_max, lon_min, lon_max);
        long end_trapdoor = System.currentTimeMillis();
        System.out.println("Trapdoor is ok !");
        System.out.println("Trapdoor time = "+(end_trapdoor-start_trapdoor) +" ms");


        List<ProofNode> NN = new ArrayList<>();
        List<byte[]> HV_list = new ArrayList<>();
        List<Double> list_height = new ArrayList<>();
        List<Long> time_query = new ArrayList<>();
        List<Long> time_proof = new ArrayList<>();

        new Query().Query_Tree(saveTree, trapdoorGen, NN, HV_list, list_height, time_query, time_proof);

        long query = 0;
        for (long e: time_query) {
            query = query+e;
        }
        long proof = 0;
        for (long e: time_proof) {
            proof = proof+e;
        }
        System.out.println("Query time = "+ query +" ms");
        System.out.println("Generate proof time = "+ proof +" ms");


//        System.out.println("NN.size()= "+ NN.size());


        long start_verify = System.currentTimeMillis();
        System.out.println("Verify: ");
        System.out.println("--------------------------------------------------");
        int correct_number = 0;
        for (ProofNode node:NN
             ) {
//            String decode = MyUtil.AESUtil.decryptByHexString(node.ciper, keysingle[0]);
//            System.out.println(node.tag);

            if (node.tag == 1){
                boolean verify_freshness = Verification.verify_Freshness(node);
                System.out.println("verify_freshness = "+verify_freshness);
                boolean verify_validity = Verification.verify_Validity(node, etree.ZKPparameters);
                System.out.println("verify_validity = "+verify_validity);
                if (verify_freshness&&verify_validity){
                    correct_number++;
                }
            }
        }

        System.out.println("correct_number = "+correct_number);

        System.out.println("--------------------------------------------------");

        String user_input = lat_min+"**"+lat_max+"**"+lon_min+"**"+lon_max;
        boolean correctness_decrypt = Verification.verify_Correctness_Decrypt(NN, user_input, keysingle[0]);
        System.out.println("correctness_decrypt = "+correctness_decrypt);

        System.out.println("--------------------------------------------------");

        boolean verify_correctness_hv = Verification.verify_Correctness_HV(list_height, saveTree.height, HV_list, saveTree.HV);
        System.out.println("verify_correctness_hv = "+verify_correctness_hv);

        System.out.println("--------------------------------------------------");

        List<Long> list_time_read = new ArrayList<>();
        boolean verify_completeness = Verification.verify_Completeness(NN, trapdoorGen, keylist, etree.HVKey,list_time_read);
        System.out.println("verify_completeness = "+verify_completeness);

        long read = 0;
        for (long e: list_time_read) {
            read = read+e;
        }
        long end_verify = System.currentTimeMillis();

        System.out.println("Verification time = " + (end_verify-start_verify-read) +" ms");

    }
}
