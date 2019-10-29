package chap1;

/**
 * @auther: hjy
 * @Date: 19-10-29 15:59
 * @Description:
 */

public class SleepMessages {

    public static void main(String[] args) throws InterruptedException{
        String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"};

        for (int i = 0; i < importantInfo.length; i++) {
            //暂停4秒
            Thread.sleep(4000);

            //打印信息
            System.out.println(importantInfo[i]);
        }
    }

}
