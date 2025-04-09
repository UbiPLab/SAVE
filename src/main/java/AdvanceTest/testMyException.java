package AdvanceTest;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/9 10:31
 * @Version 1.0
 */
import MyUtil.MyException;

import java.util.InputMismatchException;
import java.util.Scanner;
public class testMyException {
    public static void main(String[] args) {

        System.out.println();
        int age;
        Scanner input = new Scanner(System.in);
        System.out.println("请输入您的年龄：");
        try {
            age = input.nextInt();    // 获取年龄
            if(age < 0) {
                throw new MyException("您输入的年龄为负数！输入有误！");
            } else if(age > 100) {
                throw new MyException("您输入的年龄大于100！输入有误！");
            } else {
                System.out.println("您的年龄为："+age);
            }
        } catch(InputMismatchException e1) {
            System.out.println("输入的年龄不是数字！");
        } catch(MyException e2) {
            System.out.println(e2.getMessage());
        }
    }
}