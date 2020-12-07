package io.netty.example.zmc;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Test {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));
            while (true){
                SocketChannel socketChannel=serverSocketChannel.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
