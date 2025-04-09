package Method;

import Method.ProofNode.*;
import MyUtil.ReadFiledata;
import MyUtil.Show;
import org.bouncycastle.crypto.io.SignerOutputStream;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/5 16:03
 * @Version 1.0
 */
public class Query {

    public void Query_Tree(SAVETreeNode root , String[][][] TD,  List<ProofNode> NN,
                           List<byte[]> HV_list, List<Double> list_height,
                           List<Long> time_query, List<Long> time_proof) {
//        long time = 0;

        if(root==null) return;
        if (Query_TD(TD,root,NN,HV_list,list_height,time_query,time_proof)){
//            if (root.height == 1){
//                return;
//            }
            Query_Tree(root.left,TD,NN, HV_list,list_height,time_query,time_proof);
            Query_Tree(root.right,TD,NN, HV_list,list_height,time_query,time_proof);
        }else {
            long s_proof = System.currentTimeMillis();
            ProofNode proof_U ;
            if (root.isLeaf){
                //  ULN
                proof_U = new ProofNode(2);
            }else {
                //  UNN
                proof_U = new ProofNode(0);
            }

            proof_U.address = root.address;
            proof_U.HV = root.HV;
            proof_U.height = root.height;

            proof_U.ibf_length = root.ibf_length_B2;
            proof_U.seg = root.seg_B2;
            proof_U.rb = root.rb;
            proof_U.seg_id = new int[]{0};
            proof_U.flag = root.flag;

            HV_list.add(root.HV);
            NN.add(proof_U);
            list_height.add(root.height);

            long e_proof = System.currentTimeMillis();
            time_proof.add(e_proof-s_proof);

        }

    }

    public static boolean Query_TD(String[][][] Trapdoor, SAVETreeNode root, List<ProofNode> p_tree,List<byte[]> HV_list,List<Double> list_height,
                                   List<Long> time_query, List<Long> time_proof) {
        long start = System.currentTimeMillis();
        boolean flag = false;

        long IO = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
//        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        byte[][] twinlist = ReadFiledata.readArray_byte(root.address+"ibf1\\"+root.flag+".txt");

        long end_read = System.currentTimeMillis();
        IO = IO + end_read-start_read;

        long s_proof = 0;
        long e_proof = 0;
        for (int i = 0; i < Trapdoor.length; i++) {
            //  每一行都符合
            int count = 0;  //一行的列计数
            int[] seg_id = new int[Trapdoor[i].length];
//            Show.ShowMatrix(Trapdoor[i]);
            for (int j = 0; j < Trapdoor[i].length; j++) {
                //  对 trapdoor （HMAC(w,k_i) , h_k+1) 拆分
                byte[] outbytes = toByteArray(Trapdoor[i][j][0]);
                BigInteger bi = new BigInteger(1, outbytes);
                int twinindex = bi.mod(BigInteger.valueOf(root.ibf_length_B)).intValue();      //  twins_id

                byte[] hkp1 = toByteArray(Trapdoor[i][j][1]);
                BigInteger hkp1bi = new BigInteger(1, hkp1);
                byte[] sha1bytes = MyUtil.HashFounction.H.digest(hkp1bi.xor(BigInteger.valueOf(root.rb)).toByteArray());  //sha1_xor rb
                int location = new BigInteger(1, sha1bytes).mod(BigInteger.valueOf(2)).intValue();//mod2

                //  比较
//                System.out.println("compare: "+location+","+twinindex+" == "+twinlist[location][twinindex]);
                if (twinlist[location][twinindex] == 1){
                    int div = twinindex / root.seg_B;
                    int mod = twinindex % root.seg_B;
                    if (mod == 0){
                        seg_id[j] = div;
                    }else {
                        seg_id[j] = div+1;
                    }
//                    System.out.println("Tree: "+twinlist[location][twinindex]+",Trapdoor: "+ location +",twinindex :"+twinindex);
                    count++;
                }
            }
            if (count == Trapdoor[i].length){
                //  一行都满足
                flag =true;

                //  MLN
                if (root.isLeaf){
                    s_proof = System.currentTimeMillis();
                    ProofNode proof_MLN = new ProofNode(1);
                    proof_MLN.address = root.address;
                    proof_MLN.HV = root.HV;
                    proof_MLN.height = root.height;
                    proof_MLN.seg = root.seg_B;
                    proof_MLN.rb = root.rb;
                    proof_MLN.Bulletproof = root.Bulletproof;
                    proof_MLN.ZKP = root.ZKPproof;
                    proof_MLN.ciper = root.cipertext;
                    proof_MLN.seg_id = seg_id.clone();
                    proof_MLN.flag = root.flag;
                    HV_list.add(root.HV);
                    p_tree.add(proof_MLN);
                    list_height.add(root.height);

                     e_proof = System.currentTimeMillis();
                    time_proof.add(e_proof-s_proof);
                }
                break;
            }
        }
        long end = System.currentTimeMillis();
        time_query.add(end-start-IO-(e_proof-s_proof));
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
