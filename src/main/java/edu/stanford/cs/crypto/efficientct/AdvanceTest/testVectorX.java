package edu.stanford.cs.crypto.efficientct.AdvanceTest;

import cyclops.collections.immutable.VectorX;
import edu.stanford.cs.crypto.efficientct.GeneratorParams;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Group;
import edu.stanford.cs.crypto.efficientct.algebra.BN128Point;
import edu.stanford.cs.crypto.efficientct.algebra.Group;
import edu.stanford.cs.crypto.efficientct.commitments.PeddersenCommitment;
import edu.stanford.cs.crypto.efficientct.linearalgebra.PeddersenBase;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/30 9:34
 * @Version 1.0
 */
public class testVectorX {
    public static void main(String[] args) {
        VectorX<BigInteger> vectorX = VectorX.of(BigInteger.valueOf(12),BigInteger.valueOf(6),BigInteger.valueOf(10));
        System.out.println(vectorX);
        VectorX<Integer> range = VectorX.range(0, 4);
        System.out.println(range);


        VectorX<BigInteger> materialize = vectorX.materialize();
        System.out.println(materialize);

        Group curve = new BN128Group();
        GeneratorParams<BN128Point> parameters = GeneratorParams.generateParams(4, curve);
        PeddersenBase<BN128Point> base = parameters.getBase();
        VectorX<PeddersenCommitment<BN128Point>> map = vectorX.map(x -> new PeddersenCommitment<>(base, x));
//        for (PeddersenCommitment p:map) {
//            System.out.println(p.getX()+" "+p.getR());
//        }
        //  map(i -> witness.get(i / bitsPerNumber).getX().testBit(i % bitsPerNumber) ? BigInteger.ONE : BigInteger.ZERO)

        int bitsPerNumber = 4;
        VectorX<BigInteger> bigIntegers = range.map(i -> map.get(i / bitsPerNumber).getX().testBit(i % bitsPerNumber) ? BigInteger.ONE : BigInteger.ZERO);
        System.out.println(bigIntegers);

        VectorX<PeddersenCommitment<BN128Point>> materialize1 = map.materialize();
//        for (PeddersenCommitment p:materialize1) {
//            System.out.println(p.getX()+" "+p.getR());
//        }
    }
}
