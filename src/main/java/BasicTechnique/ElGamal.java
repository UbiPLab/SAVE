package BasicTechnique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/15 9:35
 * @Version 1.0
 */
public class ElGamal {

    /*
    1.随机地选择一个大素数p，且要求p-1有大素数因子，将p公开。

　　2.选择一个模p的原根α，并将α公开。

　　3.随机地选择一个整数d（1＜d＜p-1）作为私钥，并对d保密。

　　4.计算公钥y=α^d(mod p)，并将y公开。
     */

    /*
    加密

    1.随机地选取一个整数k（1＜k＜p-1）。

　　2.计算U=y^k(mod p)、C1=α^k(mod p)、C2=U^M(mod p)。

　　3.取(C1,C2)作为密文。
     */

    /*
    解密

    1.计算V=C1^d(mod p)。

　　2.计算M=C2V^-1(mod p)。
     */

    /*
    实现ELGamal算法，需要实现以下几个部分：

　　1.对大数的素数判定；

　　2.判断原根；

　　3.模指运算；

　　4.模逆运算。
     */

    /**
     * 加密一个消息m(m为BigInteger类型) 并返回加密结果为一个BigInteger数组
     * @param m
     * @param p
     * @param b
     * @param g
     * @return
     */
    public static BigInteger [] encrypt(BigInteger m,BigInteger p,BigInteger b,BigInteger g)
    {
        //随机选取一个k gcd(k,p-1)=1 0<=k<p
        BigInteger [] rtn = {null,null};//定义一个BigInteger数组,存贮返回的加密对象C1,C2
        BigInteger k = ElGamal.getRandomk(p); //随机取满足条件的k
        //计算密文C1,C1
        BigInteger C1 = g.modPow(k, p);
        BigInteger C2 = m.multiply(b.modPow(k, p)).mod(p);
        //保存密文到对象中
        rtn[0] = C1;
        rtn[1] = C2;
        return rtn;
    }
    /**
     * 取随机数k
     * @param p
     * @return
     */
    public static BigInteger getRandomk(BigInteger p)
    {
        ///随机取一个与p-1互质的数k & 0<=k<p-1
        Random r = new Random();
        BigInteger k = null;
        while(true)
        {
            k = new BigInteger(p.bitLength()-1,r);//产生一0<=k<p-1的随机数
            if(k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
            {//如果随机数与p-1互质 则选取成功,返回随机数k
                break;
            }
        }
        return k;
    }
    /**
     * 取一个大的随机素数P,和P的生成元a
     * @param alpha 为该素数的比特位数
     * @return
     */
    public static BigInteger [] getRandomP(int alpha)
    {
        BigInteger rtn [] = {null,null};
        Random r = new Random();
        BigInteger p = null;
        BigInteger q = null;
        //选取一个安全素数Q, p = 2q+1 如果P为素数,选定成功
        while(true)
        {
            q = BigInteger.probablePrime(alpha, r);//new BigInteger(alpha,r); //取一个随机数q, r为随机数发生器 alpha 为想要得到随机数大小[2^alpha]p = 2^alpha
            if(q.bitLength() != alpha) //判断生成的随机数q<2^alpha 如果q<2^alpha 重新再生成一个随机数直到q>2^alpha
                continue;
            if(q.isProbablePrime(10)) //如果q为素数则再进一步计算生成元
            {
                p = q.multiply(new BigInteger("2")).add(BigInteger.ONE); // 选取一个安全素数P=2*Q+1
                if(p.isProbablePrime(10)) //如果P为素数则选取成功 否则继续第一步
                    break;
            }
        }
        //计算p 的乘法群
        //在Zp中选择一个g != 1
        BigInteger g = null;
        while(true)
        {
            g = BigInteger.probablePrime(p.bitLength()-1, r);//new BigInteger(p.bitLength()-1,r);//从Zp*中随机取出一个元
            if(!g.modPow(BigInteger.ONE, p).equals(BigInteger.ONE) && !g.modPow(q, p).equals(BigInteger.ONE))
            {//在Z*p中任选一元素a!=1,计算a^2 mod P 和a^Q mod P ,如它们都不等于1,则a是生成元,否则继续选取
                break;
            }
        }
        rtn[0] = p;
        rtn[1] = g;
        return rtn;
    }
    /**
     * 取随机数a
     * @param p
     * @return
     */
    public static BigInteger getRandoma(BigInteger p)
    {
        BigInteger a = null;
        Random r = new Random();
        a = new BigInteger(p.bitLength()-1,r);
        return a;
    }
    /**
     * 计算b的值
     * @param g
     * @param a
     * @param p
     * @return
     */
    public static BigInteger calculateb(BigInteger g,BigInteger a,BigInteger p)
    {
        BigInteger b = null;
        b = g.modPow(a, p);
        return b;
    }
    /**
     * 解密一个串返回值为一个BigInteger对象
     * @param C1
     * @param C2
     * @param a
     * @param p
     * @return
     */
    public static BigInteger decrypt(BigInteger C1,BigInteger C2,BigInteger a,BigInteger p)
    {
        BigInteger m = null;
        m = C2.multiply(C1.modPow(a.negate(), p)).mod(p);
        return m;
    }
    /**
     * 加密一个数字签名,其实就是加密一个字符串,消息m为一个字串,程序中是把消息m(String)转成一个byte数组,
     * 然后再把byte类型的数组再构造成一个BigInteger对象,使用encrypt方法加密
     * @param m
     * @param p
     * @param b
     * @param g
     * @return
     */
    public static BigInteger [] encryptSignature(String m,BigInteger p,BigInteger b,BigInteger g)
    {
        BigInteger [] rtn = {null,null};//定义一个BigInteger数组,存贮返回的加密对象C1,C2
        BigInteger message = new BigInteger(m.getBytes());//把字串转成一个BigInteger对象
        rtn = ElGamal.encrypt(message, p, b, g);//调用解密方法encrypt解密
        return rtn;
    }
    /**
     * 解密一个数字签名,传入先前加密结果,两个BigInteger的对象C1,C2
     * 程序使用先前的decrypt方法进行解密,把解密得到的结果(BigInteger对象),转化为一个byte的数组
     * 再把byte类型的数组还原为一个字串
     * @param C1
     * @param C2
     * @param a
     * @param p
     * @return
     */
    public static String decryptSignature(BigInteger C1,BigInteger C2,BigInteger a,BigInteger p)
    {
        BigInteger rtn = ElGamal.decrypt(C1, C2, a, p);//调用decrypt方法解密
        String str = new String(rtn.toByteArray());//把返回的结果还原成一个字串
        return str;
    }
    public static void main(String [] args)
    {
        BigInteger p = null; //随机数P
        BigInteger g = null; //P的生成元
        //BigInteger m = new BigInteger("11111111111111"); // 明文M 0<=m<p
        //System.out.println("明文:"+m);//输出
        BigInteger b = null; // 公钥<b,g,p>
        BigInteger a = null;//私钥<a,g,p> 0<a<p
        BigInteger [] rtn = {null,null};

        String signm = "";
        System.out.println("请输入消息M:");
        InputStream clav= System.in;
        BufferedReader rd = new BufferedReader(new InputStreamReader(clav));
        try {signm = rd.readLine();}
        catch(IOException e) {System.out.println(e.getMessage());}
        //System.out.println(new BigInteger(signm.getBytes()).bitLength());
        //System.exit(0);
        rtn = ElGamal.getRandomP(new BigInteger(signm.getBytes()).bitLength());//取得随机数P,和P的生成元g

        p = rtn[0];
        g = rtn[1];
        a = ElGamal.getRandoma(p);
        b = ElGamal.calculateb(g, a, p);
        //rtn = ElGamal.encrypt(m, p, b, g);
        //System.out.println("密文:"+rtn[0]+","+rtn[1]);
        //BigInteger dm = ElGamal.decrypt(rtn[0], rtn[1], a, p);
        //System.out.println("解密:"+dm);
        //数字签名
        System.out.println("原文:"+signm);
        byte[] mb = signm.getBytes();
        System.out.println("字节码构造的超大整数:"+new BigInteger(mb).toString());
        System.out.println("素数P:"+p);
        System.out.println("生成元:"+g);
        System.out.println("随机数a(私钥):"+a);
        System.out.println("b(公钥):"+b);
        //rtn = ElGamal.getRandomP(100);//取得随机数P,和P的生成元g
        //p = rtn[0];
        //g = rtn[1];
        a = ElGamal.getRandoma(p);
        b = ElGamal.calculateb(g, a, p);
        rtn = ElGamal.encryptSignature(signm, p, b, g);
        System.out.println("密文:"+rtn[0]+","+rtn[1]);
        String designm = ElGamal.decryptSignature(rtn[0], rtn[1], a, p);
        mb = designm.getBytes();
        System.out.println("解密后的超大整数:"+new BigInteger(mb));
        System.out.println("解密:"+designm);

    }
}
