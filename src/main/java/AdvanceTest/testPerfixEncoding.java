package AdvanceTest;

import BasicTechnique.PrefixEncoding;
import MyUtil.Show;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/9 10:10
 * @Version 1.0
 */
public class testPerfixEncoding {
    public static void main(String[] args) throws Exception {
//        int element = 16;
//        int a = 1;
//        String[] prefix = PrefixEncoding.prefix(5, element);
//        Show.showString_list(prefix);
//        String[] range = PrefixEncoding.range(5, a, element);
//        Show.showString_list(range);
//
//        String s_4 = Integer.toBinaryString(4);
//        String s_5 = Integer.toBinaryString(15);
//        System.out.println(s_4+"+"+s_5);
//        System.out.println(PrefixEncoding.prefix(s_4,s_5));
//        String[] a = {"a","b","c"};
//        String[] b = {"d","b","c"};
//        String[] merge = PrefixEncoding.Merge(a, b);
//        Show.showString_list(merge);

//        int[] ints = {0,1,5,6,9,12,15};
//        String[] complement = PrefixEncoding.Complement(5, ints, 0, 15);
//        Show.showString_list(complement);
        String[] S = PrefixEncoding.Prefix(6,18);
        Show.showString_list(S);
        String[] S2 = PrefixEncoding.range(6,16,23);
        Show.showString_list(S2);
    }
}
