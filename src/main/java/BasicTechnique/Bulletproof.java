package BasicTechnique;

import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.VerificationFailedException;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Group;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Point;
import edu.stanford.cs.crypto.efficientct.algebra.Group;
import edu.stanford.cs.crypto.efficientct.commitments.PeddersenCommitment;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProof;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofProver;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofVerifier;
import edu.stanford.cs.crypto.efficientct.util.ProofUtils;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/4/5 9:05
 * @Version 1.0
 */
public class Bulletproof {
    public static final int N = 4;  //  2^N
    public static boolean ver_res = false;
    public static Object[] genProof(int num){
        Group<BN128Point> group = new BN128Group();

        BigInteger randomness = ProofUtils.randomNumber();

        BigInteger number = BigInteger.valueOf(num);
        GeneratorParams<BN128Point> parameters = GeneratorParams.generateParams(N,group);

        BN128Point v = parameters.getBase().commit(number, randomness);
        PeddersenCommitment<BN128Point> witness = new PeddersenCommitment<>(parameters.getBase(),number, randomness);
        RangeProofProver<BN128Point> prover = new RangeProofProver<>();
        RangeProof<BN128Point> proof = prover.generateProof(parameters, v, witness);
        Object[] objects = new Object[3];
        objects[0] = parameters;
        objects[1] = v;
        objects[2] = proof;
        return objects;
    }

    public static boolean verifyProof(GeneratorParams<BN128Point> parameters,BN128Point v,RangeProof<BN128Point> proof) throws VerificationFailedException {
        RangeProofVerifier<BN128Point> verifier = new RangeProofVerifier<>();
        verifier.verify(parameters, v, proof);
        return ver_res;
    }

}
