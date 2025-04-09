package edu.stanford.cs.crypto.efficientct.AdvanceTest;

import cyclops.collections.immutable.VectorX;
import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.VerificationFailedException;
import edu.stanford.cs.crypto.efficientct.algebra.*;
import edu.stanford.cs.crypto.efficientct.commitments.PeddersenCommitment;
import edu.stanford.cs.crypto.efficientct.linearalgebra.GeneratorVector;
import edu.stanford.cs.crypto.efficientct.linearalgebra.PeddersenBase;
import edu.stanford.cs.crypto.efficientct.multirangeproof.MultiRangeProofProver;
import edu.stanford.cs.crypto.efficientct.multirangeproof.MultiRangeProofVerifier;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProof;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofProver;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofVerifier;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/29 16:23
 * @Version 1.0
 */
public class testMultiRangeProof {
    public static void main(String[] args) throws VerificationFailedException {
        Group curve = new BN128Group();
        GeneratorParams<BN128Point> parameters = GeneratorParams.generateParams(4, curve);

//        VectorX<BigInteger> vectorX = VectorX.of(BigInteger.valueOf(12),BigInteger.valueOf(46));
        VectorX<BigInteger> vectorX = VectorX.of(BigInteger.valueOf(12),BigInteger.valueOf(6));
        PeddersenBase<BN128Point> base = parameters.getBase();
        VectorX<PeddersenCommitment<BN128Point>> witness = vectorX.map(x -> new PeddersenCommitment<>(base, x)).materialize();
        for (PeddersenCommitment p: witness) {
            System.out.println(p.getX()+" "+p.getR());
        }
        GeneratorVector<BN128Point> commitments = GeneratorVector.from(witness.map(PeddersenCommitment::getCommitment), curve);


        MultiRangeProofProver<BN128Point> prover = new MultiRangeProofProver<>();
        RangeProof<BN128Point> proof = prover.generateProof(parameters, commitments, witness);
//        RangeProof<BN128Point> singlePRoof = new RangeProofProver<BN128Point>().generateProof(parameters, commitments.get(0), witness.get(0));
//        RangeProofVerifier<BN128Point> verifier = new RangeProofVerifier<>();
//        verifier.verify(parameters, commitments.get(0), singlePRoof);

//        verifier.verify(parameters, commitments.get(0), proof);
        MultiRangeProofVerifier<BN128Point> multiRangeProofVerifier = new MultiRangeProofVerifier<>();
        multiRangeProofVerifier.verify(parameters, commitments, proof);
//        multiRangeProofVerifier.verify(parameters, commitments, singlePRoof);
        System.out.println(proof.serialize().length);
    }
}
