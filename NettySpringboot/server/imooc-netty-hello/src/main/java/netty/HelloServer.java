package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: 实现客户端发送一个请求，服务器会返回Hello Netty
 */
public class HelloServer {

    /*
    程序入口
     */
    public static void main(String[] args) throws Exception {
        // 定义一对线程组
        // 主线程组，用于接收客户端的链接，但是不做任何处理，跟老板一样不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组，老板线程组会把任务丢给他，让手下线程组去做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // netty服务器的创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();// 服务启动类
            serverBootstrap.group(bossGroup, workerGroup) // 设置主从线程组
                    .channel(NioServerSocketChannel.class) // 设置nio的双向通道
                    .childHandler(new HelloServerInitializer()); // 子处理器，用于处理workerGroup

            // 启动server，并且设置8088为启动端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
