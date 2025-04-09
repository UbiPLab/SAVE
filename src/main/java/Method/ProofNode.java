package Method;

import BasicTechnique.IBF;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/5 16:05
 * @Version 1.0
 */
public class ProofNode {

    //  IBF address
    public String address;
    //  Node HashValue
    public byte[] HV;
    //  Node height
    public double height;
    //  Node encrypted data
    public String ciper;
    //  Proof node type
    public int tag; //  0 UNN（unmatched non-leaf node）,  1 MLN,  2 ULN（unmatched leaf node）
    public BigInteger[] ZKP;
    public Object[] Bulletproof;
//    public IBF ibf;
    //  IBF B/B2 length
    public int ibf_length;
    //  IBF each segment length
    public int seg;
    //  IBF proof segment id array
    public int[] seg_id;
    //  IBF proof segment hash value array
    public byte[][] hv;
    //  Node random number
    public int rb;
    public int flag;

    public ProofNode(int tag) {
        this.tag = tag;
    }
}
