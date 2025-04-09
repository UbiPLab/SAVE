package Method;

import BasicTechnique.Data_Point;
import BasicTechnique.IBF;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/17 11:15
 * @Version 1.0
 */
public class SAVETreeNode {

    //  data formate : lat  lon  time  0/1

    public boolean isLeaf;
    public String address;
    public String[][] data;
    public String cipertext;
    public String[] cubeCoding;
//    public String address_cubeCoding;
    public int rb;
    public int rb_2;
    public byte[] HV;
    public String address_HV;
    public double height;
    public SAVETreeNode father;
    public SAVETreeNode left;
    public SAVETreeNode right;
    public BigInteger[] ZKPproof;
    public Object[] Bulletproof;
    public int flag;
    public int ibf_length_B;
    public int ibf_length_B2;
    public int seg_B;
    public int seg_B2;
    public IBF ibf_1;
    public IBF ibf_2;

    public SAVETreeNode(String[][] s) {//构造方法
        data = s;
    }
    public void showData(){
        for (int i = 0; i < data.length; i++) {
            if (i != data.length-1) {
                System.out.print(data[i][0] + ",");
            }else {
                System.out.println(data[i][0]);
            }
        }
    }
}
