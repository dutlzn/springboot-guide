package com.lzn.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;

@Component
public class WSServer {
    // 保证单例
    private static class SingletionWSServer {
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletionWSServer.instance;
    }

    // 变量
    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    // 构造器
    public WSServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    public void start() {
        this.future = server.bind(8088); //不是在main方法里面 不需要同步 sync()
        System.err.println("netty websocket server 启动完毕....");// 显示红色的
    }


//    public static void main(String[] args) throws Exception{
//        EventLoopGroup mainGroup = new NioEventLoopGroup();
//        EventLoopGroup subGroup = new NioEventLoopGroup();
//
//        try{
//            ServerBootstrap server = new ServerBootstrap();
//            server.group(mainGroup, subGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(null);
//
//            ChannelFuture future = server.bind(8088).sync();
//
//            future.channel().closeFuture().sync();
//
//        } finally {
//            mainGroup.shutdownGracefully();
//            subGroup.shutdownGracefully();
//        }
//
//    }
}
