package chap1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther: hjy
 * @Date: 19-10-30 15:44
 * @Description: 阻塞IO+线程池模式
 * 存在问题： 在大量短连接的场景中性能会有提升，因为不用每次都创建和销毁线程，而是重用连接池中的线程。
 * 但在大量长连接的场景中，因为线程被连接长期占用，不需要频繁地创建和销毁线程，因为没有什么优势.
 */

public class ThreadPoolEchoServer {


    public static int DEFAULT_PORT = 1027;

    public static void main(String[] args) throws IOException {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        }catch (RuntimeException ex){
            port = DEFAULT_PORT;
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        Socket clientSocket = null;

        try (ServerSocket serverSocket = new ServerSocket(port);){

            while (true){
                clientSocket = serverSocket.accept();

                //线程池
                threadPool.submit(new Thread(new EchoServerHandler(clientSocket)));
                new Thread().start();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Exception caught when trying to listen on port "+ port +
                    "or listening for a connection");
            System.out.println(e.getMessage());
        }


    }


}
