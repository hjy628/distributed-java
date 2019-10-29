package chap1;

/**
 * @auther: hjy
 * @Date: 19-10-29 15:57
 * @Description:
 */

public class HelloThread extends Thread{

    @Override
    public void run() {
        System.out.println("Hello from thread!");
    }


    public static void main(String[] args) {
        (new HelloThread()).start();
    }

}
