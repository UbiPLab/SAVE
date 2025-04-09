package edu.stanford.cs.crypto.efficientct.AdvanceTest;

import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.VerificationFailedException;
import edu.stanford.cs.crypto.efficientct.algebra.*;
import edu.stanford.cs.crypto.efficientct.commitments.PeddersenCommitment;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProof;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofProver;
import edu.stanford.cs.crypto.efficientct.rangeproof.RangeProofVerifier;
import edu.stanford.cs.crypto.efficientct.util.ProofUtils;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/28 10:46
 * @Version 1.0
 */
public class testRangeProof {
    public static void main(String[] args) throws VerificationFailedException {

        //  method 01
        Group<BN128Point> group = new BN128Group();
        long currentTimeMillis = System.currentTimeMillis();
        BigInteger number = BigInteger.valueOf(2).pow(4).subtract(BigInteger.ONE);
        BigInteger randomness = ProofUtils.randomNumber();

        GeneratorParams<BN128Point> parameters = GeneratorParams.generateParams(4,group);

        BN128Point v = parameters.getBase().commit(number, randomness);
        PeddersenCommitment<BN128Point> witness = new PeddersenCommitment<>(parameters.getBase(),number, randomness);

        RangeProofProver<BN128Point> prover = new RangeProofProver<>();
        RangeProof<BN128Point> proof = prover.generateProof(parameters, v, witness);
        long ii = System.currentTimeMillis();
        System.out.println("gen =" + (ii-currentTimeMillis));
        RangeProofVerifier<BN128Point> verifier = new RangeProofVerifier<>();
        verifier.verify(parameters, v, proof);
        long end = System.currentTimeMillis();
        System.out.println("ver =" +(end-ii));

        //  method 02
//        Group<?> curve =new C0C0Group();
////        Group<?> curve =new C0C0Group();
//        BigInteger number = BigInteger.valueOf(5);
//        BigInteger randomness = ProofUtils.randomNumber();
//
//        GeneratorParams parameters = GeneratorParams.generateParams(256,curve);
//        GroupElement v = parameters.getBase().commit(number, randomness);
//        PeddersenCommitment<?> witness = new PeddersenCommitment<>(parameters.getBase(),number, randomness);
//        BouncyCastleECPoint.addCount=0;
//        BouncyCastleECPoint.expCount=0;
//        RangeProof proof = new RangeProofProver().generateProof(parameters, v, witness);
//        System.out.println(BouncyCastleECPoint.expCount);
//        System.out.println(BouncyCastleECPoint.addCount);
//        RangeProofVerifier verifier = new RangeProofVerifier();
//        verifier.verify(parameters, v, proof);
    }
}
