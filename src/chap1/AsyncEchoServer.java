package chap1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @auther: hjy
 * @Date: 19-10-30 16:48
 * @Description:  异步I/O模式
 */

public class AsyncEchoServer {

    public static int DEFAULT_PORT = 1027;

    public static void main(String[] args) throws IOException {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        }catch (RuntimeException ex){
            port = DEFAULT_PORT;
        }

        System.out.println("Listening for connections on port "+ port);

        ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        //创建异步服务器socket channel 并绑定到默认组
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()){
            if (asynchronousServerSocketChannel.isOpen()){
                //设置一些参数
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,4*1024);
                asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR,true);

                //绑定服务器socket channel到本地地址
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));

                //显示等待客户端的信息
                System.out.println("Waiting for connections ...");
                while (true){
                    Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture
                            = asynchronousServerSocketChannel.accept();

                    try {
                        final AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();
                        Callable<String> worker = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String host = asynchronousSocketChannel.getRemoteAddress().toString();
                                System.out.println("Incoming connection from: "+ host);
                                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                                //发送数据
                                while (asynchronousSocketChannel.read(buffer).get() != -1){
                                    buffer.flip();
                                    asynchronousSocketChannel.write(buffer).get();
                                    if (buffer.hasRemaining()){
                                        buffer.compact();
                                    }else {
                                        buffer.clear();
                                    }
                                }
                                asynchronousSocketChannel.close();;
                                System.out.println(host+"was successfully served!" );
                                return host;
                            }
                        };
                        taskExecutor.submit(worker);
                    }catch (InterruptedException | ExecutionException ex){
                        System.err.println(ex);
                        System.err.println("\n Server is shutting down...");

                        //执行器不再接收新线程
                        //并完成队列中所有的线程
                        taskExecutor.shutdown();

                        //等待所有线程完成
                        while (!taskExecutor.isTerminated()){

                        }
                        break;
                    }
                }
            }else {
                System.out.println("The asynchronous server-socket channel cannot be opend!");
            }
        }catch (IOException ex){
            System.err.println(ex);
        }
    }
}
