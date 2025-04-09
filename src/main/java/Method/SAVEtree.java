package Method;

import BasicTechnique.*;
import MyUtil.AESUtil;
import MyUtil.HashFounction;
import MyUtil.ReadFiledata;

import java.math.BigInteger;
import java.util.*;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/15 15:24
 * @Version 1.0
 */
public class SAVEtree {


    int round;
    public double p = 0.01;
    public String Save_address;

    public String[] Keylist;
    public String AESKey;
    public String HVKey;
    public String CubeKey;
    public String ZKPKey;
    public int L = 8;


    public BigInteger[] ZKPparameters;  //  r q p g

    public boolean initZKP(int alpha){
//        ZKPparameters = ZKP.getParameter(alpha);
        ZKPparameters = new BigInteger[]{BigInteger.valueOf(1019), BigInteger.valueOf(2039), BigInteger.valueOf(4079), BigInteger.valueOf(379)};
        return true;
    }

    public SAVETreeNode BuildSaveTree_new(String[][] nums,int height){
        //  空树
        if(nums.length==0)
            return null;
        //  叶子节点
        if (nums.length==1){
            SAVETreeNode root = new SAVETreeNode(nums);
            root.flag = round++;
            if (height>1)
                root.height = height;
            else
                root.height = 1;
            root.rb = round;
            root.rb_2 = round;
            root.isLeaf = true;
            return root;
        }
        //  正常节点
        SAVETreeNode root = new SAVETreeNode(nums);
        root.height = height;
        //  计算左子树元素个数

        int num_left = (int) Math.pow(2,(Math.ceil(Math.log(nums.length) / Math.log(2)))-1);
        //  删除原树元素集合中右子树元素，剩余为左子树元素
        String[][] tree_left = new String[num_left][];
        String[][] tree_right = new String[nums.length-num_left][];
        for (int i = 0; i < num_left; i++) {
            tree_left[i] = Arrays.copyOfRange(nums[i],0,nums[i].length);
        }
        for (int i = num_left; i < nums.length; i++) {
            tree_right[i-num_left] = Arrays.copyOfRange(nums[i],0,nums[i].length);
        }
        root.flag = round++;
        root.rb = round;
        root.rb_2 = round;
        root.isLeaf = false;
//        root.flag = new Random().nextInt(2000000);


        root.left = BuildSaveTree_new(tree_left,height-1);
        root.right = BuildSaveTree_new(tree_right,height-1);
        return root;
    }

//    public SAVETreeNode BuildSaveTree_old(String[][] nums){
//        //  空树
//        if(nums.length==0)
//            return null;
//        //  叶子节点
//        if (nums.length==1){
//            SAVETreeNode root = new SAVETreeNode(nums);
//            root.flag = round++;
//            root.isLeaf = true;
//            root.height = 1;
//            return root;
//        }
//        //  正常节点
//
//        //  计算左子树元素个数
//        int num_left = 0;
//        if (nums.length%2 == 0){
//            num_left = nums.length/2;
//        }else {
//            num_left = nums.length/2 + 1;
//        }
//        //  删除原树元素集合中右子树元素，剩余为左子树元素
//        String[][] tree_left = new String[num_left][];
//        String[][] tree_right = new String[nums.length-num_left][];
//        for (int i = 0, j = nums.length - 1; i <=j ; i++,j--) {
//            if (i==j) {
//                tree_left[i] = nums[i];
//            }else {
//                tree_left[i] = nums[i];
//                tree_right[j-num_left] = nums[j];
//            }
//        }
//        SAVETreeNode root = new SAVETreeNode(nums);
//        root.flag = round++;
//        root.rb = round;
//        root.rb_2 = round;
////        root.flag = new Random().nextInt(2000000);
//        root.height = (Math.ceil(Math.log(nums.length) / Math.log(2))) + 1;
//        root.isLeaf = false;
//        root.left = BuildSaveTree_old(tree_left);
//        root.right = BuildSaveTree_old(tree_right);
//        return root;
//    }

//    public long initNode(SAVETreeNode root ,  List<Long> sizeofCS) throws Exception {
//        long time = 0;
//        if(root==null)
//            return time;
//        time = time + initNode(root.left,sizeofCS);
//        time = time + initNode(root.right,sizeofCS);
//
//        if (root.height != 1){
//            time = time + initMidNode(root,sizeofCS);
//        }else  if (root.height == 1){
//            time = time + initLeafNode(root,sizeofCS);
//        }
////        System.out.println(root.address + "is ok");
//        return time;
//    }

    public long initNode_new(SAVETreeNode root ,  List<Long> sizeofCS) throws Exception {
        long time = 0;
        if(root==null)
            return time;
        time = time + initNode_new(root.left,sizeofCS);
        time = time + initNode_new(root.right,sizeofCS);

        if (!root.isLeaf){
            time = time + initMidNode(root,sizeofCS);
        }else {
            time = time + initLeafNode(root,sizeofCS);
        }
//        System.out.println(root.address + "is ok");
        return time;
    }



    /**
     * 初始化叶子节点
     * @param leafnode  叶子
     *
     * @throws Exception
     */
    public long initLeafNode(SAVETreeNode leafnode , List<Long> sizeofCS ) throws Exception {
        long All_start = System.currentTimeMillis();

        //  存储地址
        leafnode.address = Save_address;

        //  加密数据项
        String plaintext = String.valueOf(leafnode.data[0][0]) +"|"+ String.valueOf(leafnode.data[0][1]) +"|"+ String.valueOf(leafnode.data[0][2]) +"|"+ String.valueOf(leafnode.data[0][3]);
        leafnode.cipertext = AESUtil.encryptIntoHexString(plaintext,AESKey);

        CubeEncoding cubeEncoding = new CubeEncoding();
        double lat = Double.parseDouble(leafnode.data[0][0]);
        double lon = Double.parseDouble(leafnode.data[0][1]);
        Data_Point point = new Data_Point(lat, lon);
        //  生成Cube
//        leafnode.cubeCoding = cubeEncoding.LevelElement(CubeKey,point);
        String[] cubeCoding = cubeEncoding.LevelElement(CubeKey,point);
//        leafnode.cubeCoding = cubeCoding;
        //  生成Cube补集
        String[] complementary = cubeEncoding.LevelElement_Complementary(CubeKey, point);

        //  插入IBF B
        double ibf_length_B = BloomFilterCal.Compute_M(cubeCoding.length, p);

        IBF ibf_B = new IBF(Keylist, (int) ibf_length_B, leafnode.rb);

        for (String cube:cubeCoding) {
            ibf_B.insert(cube);
        }
        leafnode.ibf_length_B = (int) ibf_length_B;
        leafnode.ibf_1 = ibf_B;
        //  插入IBF B'
        double ibf_length_B2 = BloomFilterCal.Compute_M(complementary.length, p);
        IBF ibf_B2 = new IBF(Keylist, (int) ibf_length_B2, leafnode.rb_2);
        for (String com:complementary) {
            ibf_B2.insert(com);
        }
        leafnode.ibf_length_B2 = (int) ibf_length_B2;
        leafnode.ibf_2 = ibf_B2;
        //  生成证据 ZKP
        int secret = Integer.parseInt(leafnode.data[0][3]);
        String ibf_String = ibf_B.computeHash();
//        System.out.println(secret);
//        System.out.println(ibf_String);
        BigInteger[] ZKPproof = ZKP.compute(ZKPparameters[0], ZKPparameters[1], ZKPparameters[2], ZKPparameters[3], secret, ibf_String, ZKPKey);
        leafnode.ZKPproof = ZKPproof;

        //  生成证据 Bulletproof
        int freshness = Integer.parseInt(leafnode.data[0][2]);
        Object[] objects = Bulletproof.genProof(freshness);
        leafnode.Bulletproof = objects;

        //  IBF分段
        int ibf_B_segment = DivSegment(ibf_length_B,L);
        int ibf_B2_segment = DivSegment(ibf_length_B2,L);
        leafnode.seg_B =ibf_B_segment;
        leafnode.seg_B2 = ibf_B2_segment;


        String[] strings_B = ibf_B.computeSegment(L,ibf_B_segment);
        String[] strings_B2 = ibf_B2.computeSegment(L,ibf_B2_segment);

        byte[][] bytes_seg_HV_B = new byte[L][];
        byte[][] bytes_seg_HV_B2 = new byte[L][];
        for (int i = 0; i < L; i++) {
            bytes_seg_HV_B[i] = HashFounction.HmacSHA256Encrypt(strings_B[i],HVKey);
            bytes_seg_HV_B2[i] = HashFounction.HmacSHA256Encrypt(strings_B2[i],HVKey);
        }

        //  计算HV
        byte[] HV = HashFounction.HmacSHA256Encrypt(ibf_B.computeHash(),HVKey);
        leafnode.HV = HV;

        /**
         * 添加存储cubeCoding代码
         * 添加存储ibf_B代码
         * 添加存储ibf_B2代码
         */

        long IO = 0;
        long start_write = System.currentTimeMillis();

        ReadFiledata.saveArray(cubeCoding,leafnode.address+"cubeCoding\\"+leafnode.flag+".txt");
        ReadFiledata.saveArray(ibf_B.twinlist,leafnode.address+"ibf1\\"+leafnode.flag+".txt");
        ReadFiledata.saveArray(ibf_B2.twinlist,leafnode.address+"ibf2\\"+leafnode.flag+".txt");

        long end_write = System.currentTimeMillis();
        IO = end_write-start_write;

        long All_end = System.currentTimeMillis();
        return All_end-All_start-IO;
    }

    public int DivSegment(double ibf_length, int l) {
        if (ibf_length % l == 0){
            return (int) (ibf_length/l);
        }else {
            return (int) (ibf_length/(l) +1);
        }
    }


    /**
     * 初始化中间节点
     * @param midnode   中间节点
     * @throws
     */
    public long initMidNode(SAVETreeNode midnode, List<Long> sizeofCS) throws Exception {
        long All_start = System.currentTimeMillis();

        midnode.address =Save_address;

        CubeEncoding cubeEncoding = new CubeEncoding();
        int numbers = midnode.data.length;
        Data_Point[] data_points = new Data_Point[numbers];
        for (int i = 0; i < numbers; i++) {
            double lat = Double.parseDouble(midnode.data[0][0]);
            double lon = Double.parseDouble(midnode.data[0][1]);
            data_points[i] = new Data_Point(lat,lon);
        }

        /**
         * 添加读取left，right节点cubeCoding代码
         */
        long IO_read = 0;
        long start_read = System.currentTimeMillis();
        //  读取节点twinlist
//        Byte[][] twinlist = ReadFiledata.readArray(root.address);
        String[] left_cubeCoding = ReadFiledata.readArray_Str(midnode.left.address+"cubeCoding\\"+midnode.left.flag+".txt");
        String[] right_cubeCoding = ReadFiledata.readArray_Str(midnode.right.address+"cubeCoding\\"+midnode.right.flag+".txt");
        long end_read = System.currentTimeMillis();
        IO_read = end_read-start_read;

        //  生成Cube
//        midnode.cubeCoding = PrefixEncoding.UnitPrefix(midnode.left.cubeCoding,midnode.right.cubeCoding);
//        String[] cubeCoding = midnode.cubeCoding;

        String[] cubeCoding = PrefixEncoding.UnitPrefix(left_cubeCoding,right_cubeCoding);

        //  生成Cube补集
        String[] complementary = cubeEncoding.LevelElement_Complementary(CubeKey, data_points);
        //  插入IBF B
        double ibf_length_B = BloomFilterCal.Compute_M(cubeCoding.length, p);
        IBF ibf_B = new IBF(Keylist, (int) ibf_length_B, midnode.rb);
        for (String cube:cubeCoding) {
            ibf_B.insert(cube);
        }
        midnode.ibf_length_B = (int) ibf_length_B;
//        midnode.ibf_1 = ibf_B;
        //  插入IBF B'
        double ibf_length_B2 = BloomFilterCal.Compute_M(complementary.length, p);
        IBF ibf_B2 = new IBF(Keylist, (int) ibf_length_B2, midnode.rb_2);
        for (String com:complementary) {
            ibf_B2.insert(com);
        }
        midnode.ibf_length_B2 = (int) ibf_length_B2;
//        midnode.ibf_2 = ibf_B2;
        //  IBF分段

        int ibf_B_segment = DivSegment(ibf_length_B,L);
        int ibf_B2_segment = DivSegment(ibf_length_B2,L);
        midnode.seg_B =ibf_B_segment;
        midnode.seg_B2 = ibf_B2_segment;


        String[] strings_B = ibf_B.computeSegment(L,ibf_B_segment);
        String[] strings_B2 = ibf_B2.computeSegment(L,ibf_B2_segment);

        byte[][] bytes_seg_HV_B = new byte[L][];
        byte[][] bytes_seg_HV_B2 = new byte[L][];
        for (int i = 0; i < L; i++) {
            bytes_seg_HV_B[i] = HashFounction.HmacSHA256Encrypt(strings_B[i],HVKey);
            bytes_seg_HV_B2[i] = HashFounction.HmacSHA256Encrypt(strings_B2[i],HVKey);
        }

        //  计算HV

//        byte[] HV = HashFounction.HmacSHA256Encrypt(ibf_B.computeHash()+ DataConvert.toHexString(midnode.left.HV)+DataConvert.toHexString(midnode.right.HV),HVKey);
        byte[] HV = HashFounction.mdinstance.digest(addBytes(midnode.left.HV,midnode.right.HV));
        midnode.HV = HV;

        /**
         * 添加存储cubeCoding代码
         * 添加存储ibf_B代码
         * 添加存储ibf_B2代码
         */

        long IO_write = 0;
        long start_write = System.currentTimeMillis();

        ReadFiledata.saveArray(cubeCoding,midnode.address+"cubeCoding\\"+midnode.flag+".txt");
        ReadFiledata.saveArray(ibf_B.twinlist,midnode.address+"ibf1\\"+midnode.flag+".txt");
        ReadFiledata.saveArray(ibf_B2.twinlist,midnode.address+"ibf2\\"+midnode.flag+".txt");

        long end_write = System.currentTimeMillis();
        IO_write = end_write-start_write;

        long All_end = System.currentTimeMillis();
        long time = All_end - All_start - IO_read - IO_write;
        return time;

    }


    /**
     * 将数据插入twinlist
     * @param twinlist
     * @param data
     * @param Keylist
     * @param rb
     * @throws Exception
     */
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

    //  层次遍历
    public void levelOrder(SAVETreeNode root){
        if(root==null)
            return;
        Queue<SAVETreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()){
            SAVETreeNode node = queue.poll();
//            if (node.height > 1)
//                node.showData();
            System.out.println(node.height);
            if(node.left!=null)
                queue.offer(node.left);
            if(node.right!=null)
                queue.offer(node.right);
        }
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

    /**
     * 后序遍历
     * @param node
     */
    public void behindOrder2(SAVETreeNode node){
        if(node==null)
            return;
        Stack<SAVETreeNode> s = new Stack<>();

        SAVETreeNode curNode; //当前访问的结点
        SAVETreeNode lastVisitNode; //上次访问的结点
        curNode = node;
        lastVisitNode = node;

        //把currentNode移到左子树的最下边
        while(curNode!=null){
            s.push(curNode);
            curNode = curNode.left;
        }
        while(!s.empty()){
            curNode = s.pop();  //弹出栈顶元素
            //一个根节点被访问的前提是：无右子树或右子树已被访问过
            if(curNode.right !=null && curNode.right != lastVisitNode){
                //根节点再次入栈
                s.push(curNode);
                //进入右子树，且可肯定右子树一定不为空
                curNode = curNode.right;
                while(curNode!=null){
                    //再走到右子树的最左边
                    s.push(curNode);
                    curNode = curNode.left;
                }
            }else{
                //访问
//                System.out.print(curNode.val+" " );
                //修改最近被访问的节点
                lastVisitNode = curNode;
            }
        } //while
    }


    //  后序遍历
    public void postOrder(SAVETreeNode root){
        if(root==null)
            return;
        postOrder(root.left);
        postOrder(root.right);
        root.showData();
    }

    //取并集
    public static byte together(byte a, byte b){
        byte m = 1;
        if( (a==1) || (b==1)){
            return m;
        }else {
            return a;
        }
    }
}
