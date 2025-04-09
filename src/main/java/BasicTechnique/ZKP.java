package BasicTechnique;

import MyUtil.HashFounction;

import java.math.BigInteger;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/13 16:00
 * @Version 1.0
 */
public class ZKP {

    /**
     * 取一个大的随机素数P,和P的生成元a
     * @param alpha 为该素数的比特位数
     * @return
     */
    public static BigInteger[] getParameter(int alpha)
    {
        BigInteger rtn [] = {null,null,null,null};
        Random random = new Random();
        BigInteger r = null;
        BigInteger p = null;
        BigInteger q = null;
        BigInteger k = new BigInteger("2");
        //选取一个安全素数r, q = 2r+1 , p=kq+1 k为偶数 如果p q为素数,选定成功
        while(true)
        {
            //取一个随机数r, random为随机数发生器 alpha 为想要得到随机数大小[2^alpha]
            r = BigInteger.probablePrime(alpha, random);//new BigInteger(alpha,r);
            if(r.bitLength() != alpha) //判断生成的随机数r<2^alpha 如果r<2^alpha 重新再生成一个随机数直到r>2^alpha
                continue;
            if(r.isProbablePrime(10)) //如果r为素数则再进一步计算生成元
            {
                q = r.multiply(new BigInteger("2")).add(BigInteger.ONE); // 选取一个安全素数P=2*Q+1
                if(q.isProbablePrime(10)) { //如果P为素数则选取成功 否则继续第一步
                    p = q.multiply(k).add(BigInteger.ONE);
                    if (p.isProbablePrime(10)){
                        break;
                    }
                }
            }
        }
        //计算r 的乘法群
        //在Zp中选择一个g
        BigInteger g = null;
        while(true)
        {
            g = BigInteger.probablePrime(r.bitLength()-1, random);//new BigInteger(p.bitLength()-1,r);//从Zp*中随机取出一个元
            if(!g.modPow(BigInteger.ONE, r).equals(BigInteger.ONE) && !g.modPow(q, r).equals(BigInteger.ONE))
            {//在Z*p中任选一元素a!=1,计算a^2 mod P 和a^Q mod P ,如它们都不等于1,则a是生成元,否则继续选取
                break;
            }
        }
        rtn[0] = r;
        rtn[1] = q;
        rtn[2] = p;
        rtn[3] = g;
        return rtn;
    }

    public static BigInteger[] compute(BigInteger r, BigInteger q, BigInteger p, BigInteger g, int secret, String ibf, String sk2) throws Exception {
        Random random = new Random();
        //  选取元素f
        BigInteger f = new BigInteger(String.valueOf(random.nextInt(r.intValue()) + 1)) ;

        //  选取元素z z in Zq
        BigInteger z = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1)) ;

        //  计算F
        BigInteger F;
        if (secret==1) {
            F = g.pow(secret).multiply(f.pow(z.intValue()));
        }else {
            F = f.pow(z.intValue());
        }

        //  选取e,u2,v2 in Zq
        BigInteger e = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1)) ;
        BigInteger u2 = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1)) ;
        BigInteger v2 = new BigInteger(String.valueOf(random.nextInt(q.intValue()) + 1)) ;

//        BigInteger A = (f.modPow(u2,q).multiply(F.modPow(v2.negate(),q))).mod(p);
//        BigInteger B = (f.modPow(e,q)).mod(p);

        BigInteger A = f.modPow(u2,p).multiply(F.modPow(v2.negate(),p)).mod(p);
        BigInteger B = f.modPow(e,p);


//        long timestamp = System.currentTimeMillis();
        long timestamp = 1;
        String tp_Bi = timestamp + ibf;

        BigInteger randomChallenge = new BigInteger(HashFounction.HmacSHA256Encrypt(tp_Bi,sk2)).mod(q);

        BigInteger v1 = randomChallenge.subtract(v2).mod(q);
        BigInteger u1 = e.add(z.multiply(v1)).mod(q);

        BigInteger[] res = new BigInteger[9];
        res[0] = A;
        res[1] = B;
        res[2] = u1;
        res[3] = u2;
        res[4] = v1;
        res[5] = v2;
        res[6] = f;
        res[7] = F;
        res[8] = randomChallenge;

//        BigInteger rc_2 = v1.add(v2).mod(q);
//        BigInteger f_u1 = B.multiply((F.divide(g)).modPow(v1,p)).mod(p);
//        BigInteger f_u2 = A.multiply(F.modPow(v2,p)).mod(p);
//        BigInteger f_o = f.modPow(u1, p);
//        BigInteger f_k = f.modPow(u2, p);
//        System.out.println("randomChallenge: ");
//        System.out.println(randomChallenge);
//        System.out.println(rc_2);
//        System.out.println("u1: ");
//        System.out.println(f_o.intValue());
//        System.out.println(f_u1.intValue());
//        System.out.println("u2 :");
//        System.out.println(f_k.intValue());
//        System.out.println(f_u2.intValue());

        return res;
    }


}
