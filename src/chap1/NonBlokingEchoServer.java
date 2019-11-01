package chap1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @auther: hjy
 * @Date: 19-10-30 16:32
 * @Description: 主线程非阻塞I/O
 */

public class NonBlokingEchoServer {

    public static int DEFAULT_PORT = 1027;

    public static void main(String[] args) throws IOException {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        }catch (RuntimeException ex){
            port = DEFAULT_PORT;
        }

        System.out.println("Listening for connections on port "+ port);

        ServerSocketChannel serverSocketChannel;
        Selector selector;

        try{
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Exception caught when trying to listen on port "+ port +
                    "or listening for a connection");
            System.out.println(e.getMessage());
            return;
        }

        while (true){
            try {
                selector.select();
            }catch (IOException ex){
                ex.printStackTrace();
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from "+client);
                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        clientKey.attach(buffer);
                    }
                    if (key.isReadable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        client.read(output);
                    }
                    if (key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        output.flip();
                        client.write(output);

                        output.compact();
                    }
                }catch (IOException ex){
                    key.cancel();
                    try {
                        key.channel().close();
                    }catch (IOException cex){

                    }
                }
            }


        }



    }
}
