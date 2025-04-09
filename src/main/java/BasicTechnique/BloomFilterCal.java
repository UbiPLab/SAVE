package BasicTechnique;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/29 9:31
 * @Version 1.0
 */
public class BloomFilterCal {

    public static double Compute_M(int n, double p){
        return Math.ceil((n*Math.log(p))/ Math.log(1 / Math.pow(2,Math.log(2))));
    }

    public double Compute_K(int n, double m){
        return Math.round((m/n)*Math.log(2));
    }

    public static void main(String[] args) {
        BloomFilterCal bloomFilterCal = new BloomFilterCal();
        double m = bloomFilterCal.Compute_M(8192, 0.01);
        System.out.println(m);
        double k = bloomFilterCal.Compute_K(8192, (int) m);
        System.out.println(k);
    }
}
