package io.netty.example.zmc;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Test {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);//EventLoopGroup一个死循环，不停地检测IO事件，处理IO事件，执行任务
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try{
            ServerBootstrap b=new ServerBootstrap();//ServerBootstrap 是服务端的一个启动辅助类，通过给他设置一系列参数来绑定端口启动服务
            b.group(bossGroup,workerGroup)//我们需要两种类型的人干活，一个是老板，一个是工人，老板负责从外面接活，接到的活分配给工人干，放到这里，bossGroup的作用就是不断地accept到新的连接，将新的连接丢给workerGroup来处理
                    .channel(NioServerSocketChannel.class)//表示服务端启动的是nio相关的channel，channel在netty里面是一大核心概念，可以理解为一条channel就是一个连接或者一个服务端bind动作
                    .handler(new SimpleServerHandler())// 表示服务器启动过程中，需要经过哪些流程，这里SimpleServerHandler最终的顶层接口为ChannelHander，是netty的一大核心概念，表示数据流经过的处理器，可以理解为流水线上的每一道关卡
                    .childHandler(new ChannelInitializer<SocketChannel>() {//表示一条新的连接进来之后，该怎么处理，也就是上面所说的，老板如何给工人配活
                       @Override
                       public void initChannel(SocketChannel ch) throws Exception{ }
                    });
            ChannelFuture f=b.bind(8888).sync();//这里就是真正的启动过程了，绑定8888端口，等待服务器启动完毕，才会进入下行代码
            f.channel().closeFuture().sync();// 等待服务端关闭socket//它会让线程进入 wait 状态，这样服务端可以一直处于运行状态，如果没有这行代码，bind 操作之后就会进入 finally 代码块，整个服务端就退出结束了。

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    private static class SimpleServerHandler extends ChannelInboundHandlerAdapter{
           @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception{
               System.out.println("channelActive");
           }
           @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
               System.out.println("channelRegistered");
           }
           @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
               System.out.println("handlerAdded");
           }
    }

}
