package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @auther: hjy
 * @Date: 19-10-30 15:44
 * @Description: 阻塞IO+多线程模式
 */

public class MultiThreadEchoServer {


    public static int DEFAULT_PORT = 1027;

    public static void main(String[] args) throws IOException {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        }catch (RuntimeException ex){
            port = DEFAULT_PORT;
        }

        Socket clientSocket = null;
        try (ServerSocket serverSocket = new ServerSocket(port);){

            while (true){
                clientSocket = serverSocket.accept();

                //多线程
                new Thread(new EchoServerHandler(clientSocket)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Exception caught when trying to listen on port "+ port +
                    "or listening for a connection");
            System.out.println(e.getMessage());
        }


    }


}
