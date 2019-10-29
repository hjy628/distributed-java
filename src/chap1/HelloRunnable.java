package chap1;

/**
 * @auther: hjy
 * @Date: 19-10-29 15:56
 * @Description:
 */

public class HelloRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello from a runnable");
    }

    public static void main(String[] args) {
        (new Thread(new HelloRunnable())).start();

    }
}
