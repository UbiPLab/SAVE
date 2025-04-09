package AdvanceTest;

import BasicTechnique.Bulletproof;
import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.VerificationFailedException;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Point;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProof;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/21 9:11
 * @Version 1.0
 */
public class testBulletProof {

    public static void main(String[] args) throws VerificationFailedException {
        Object[] objects = Bulletproof.genProof(8);

        boolean b = Bulletproof.verifyProof((GeneratorParams<BN128Point>)objects[0], (BN128Point) objects[1], (RangeProof<BN128Point>)objects[2]);
        System.out.println(b);
    }
}
