package AdvanceTest;

import Method.SAVETreeNode;
import Method.SAVEtree;
import MyUtil.ReadFiledata;

import java.util.ArrayList;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/6/20 9:37
 * @Version 1.0
 */
public class testBuildtree {
    public static void main(String[] args) throws Exception {

        int total_number = 60;
        String[][] dataSet = new String[total_number][2];
        for (int i = 0; i < total_number; i++) {
            dataSet[i][0] = String.valueOf(i);
            dataSet[i][1] = String.valueOf(i);
        }

//        Show.ShowMatrix(dataSet);

        System.out.println("data is ready !");
        SAVEtree etree = new SAVEtree();
        etree.Save_address = "C:\\Users\\UbiP Lab Laptop 02\\Desktop\\SaveTemp\\";
//        String[] keylist = HashFounction.CreateSecretKey(16, 6);
//        String[] keylist = {"6992171125466298","0219126809305670","3177204288447043","8510240487633587","0632934334653356","7398280671752376"};
        String[] keylist = {"6992171125466298","0219126809305670","7398280671752376"};

        int height = (int) ((Math.ceil(Math.log(dataSet.length) / Math.log(2))) + 1);
        SAVETreeNode saveTree = etree.BuildSaveTree_new(dataSet,height);
        etree.levelOrder(saveTree);


    }
}
