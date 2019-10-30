package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @auther: hjy
 * @Date: 19-10-30 15:47
 * @Description: 处理器类
 * 存在问题:每次接收到新的连接都要新建一个线程，处理完后销毁线程，代价大，当有大量的短连接出现时，性能比较低
 */

public class EchoServerHandler implements Runnable{

    private Socket clientSocket;

    public EchoServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
        {
            String inputLine;
            while ((inputLine = in.readLine())!=null) {
                out.println(inputLine);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
