package com.manning.nettyinaction.chapter2; /**
 * Created by lehaozhu on 2016/8/29.
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port=port;
    }

    public void start()throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            //Specifies NIO transport, local socket address
            //Adds handler to channel pipeline
            b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
            .childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
            //Binds server, waits for server to close, and releases resources
            ChannelFuture f=b.bind().sync();
            System.out.println(EchoServer.class.getName()+"started and listen on"+f.channel().localAddress());
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
    public static void main(String[] args) throws Exception {
        if(args.length!=1){
            System.err.print("Usage:"+EchoServer.class.getSimpleName()+"<port>");
        }
        int port=Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}
