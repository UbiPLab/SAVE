package MyUtil;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2023/3/9 10:29
 * @Version 1.0
 */
public class MyException extends Exception{
    public MyException() {
        super();
    }
    public MyException(String str) {
        super(str);
    }

    public MyException(int value) {
        super(String.valueOf(value));
    }
}
