package BasicTechnique;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/15 8:40
 * @Version 1.0
 */

import java.math.BigInteger;


public class ELGama_self {


    public static BigInteger p;
    public static BigInteger alpha;
    public static BigInteger d;
    public static BigInteger y;



    /**
     * 加密
     * @param M
     * @return
     */
    BigInteger[] encrypt(BigInteger M) {
        BigInteger[] C = new BigInteger[2];
        BigInteger k, U;

        //  随机地选取一个整数k（1＜k＜p-1）
        //  计算U=y^k(mod p)、C1=α^k(mod p)、C2=U^M(mod p)
//        do {
//            do {
//                k = new BigInteger(100, new Random());
//            } while (k.compareTo(BigInteger.ONE) != 1 || k.compareTo(p.subtract(BigInteger.ONE)) != -1);
//            U = y.modPow(k, p);
//        } while (U.intValue() != 1);

        k = BigInteger.valueOf(853);
        U = y.modPow(k, p);

        C[0] = alpha.modPow(k, p);
        C[1] = U.multiply(M).mod(p);

        //  取(C1,C2)作为密文
        return C;
    }

    /**
     * 解密
     * @param C
     * @return
     */
    BigInteger decrypt(BigInteger[] C) {
        //  计算V=C1^d(mod p)
        BigInteger V = C[0].modPow(d, p);
        //  计算M=C2V^-1(mod p)
        BigInteger M = C[1].multiply(V.modPow(new BigInteger("-1"), p)).mod(p);
        return M;
    }

    /**
     * 判断a是否为模m的原根，其中m为素数
     * @param a
     * @param m
     * @return
     */
    static boolean isOrigin(BigInteger a, BigInteger m) {
        if (a.gcd(m).intValue() != 1) return false;
        BigInteger i = new BigInteger("2");
        while (i.compareTo(m.subtract(BigInteger.ONE)) == -1) {
            if (m.mod(i).intValue() == 0) {
                if (a.modPow(i, m).intValue() == 1)
                    return false;
                while (m.mod(i).intValue() == 0)
                    m = m.divide(i);
            }
            i = i.add(BigInteger.ONE);
        }
        return true;
    }

    public static void main(String[] args) throws Exception {



//        //  随机地选择一个大素数p，且要求p-1有大素数因子，将p公开。
//        do {
//            p = BigInteger.probablePrime(100, new Random());
//        } while (p.subtract(BigInteger.ONE).divide(new BigInteger("2")).isProbablePrime(100));
//        //  选择一个模p的原根α，并将α公开
//        do {
//            alpha = new BigInteger(100, new Random());
//        } while (! isOrigin(alpha, p));
//
//        //  随机地选择一个整数d（1＜d＜p-1）作为私钥，并对d保密
//        do {
//            d = new BigInteger(100, new Random());
//        } while (d.compareTo(BigInteger.ONE) != 1 || d.compareTo(p.subtract(BigInteger.ONE)) != -1);
//
//        //  计算公钥y=α^d(mod p)，并将y公开。
//        y = alpha.modPow(d, p);


        p = BigInteger.valueOf(2579);
        alpha = BigInteger.valueOf(2);
        d = BigInteger.valueOf(765);
        y = alpha.modPow(d, p);

        BigInteger M = BigInteger.valueOf(1299);
        ELGama_self elGamal = new ELGama_self();
        System.out.println("parameters:");
        System.out.println("p="+p);
        System.out.println("alpha="+alpha);
        System.out.println("d="+d);
//        System.out.println("p="+p);
        BigInteger[] cipertext = elGamal.encrypt(M);
        System.out.println("cipertext:");
        System.out.println(cipertext[0]);
        System.out.println(cipertext[1]);

        int plaintext = elGamal.decrypt(cipertext).intValue();
        System.out.println(plaintext);
    }

}

