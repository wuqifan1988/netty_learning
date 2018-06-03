package com.shengsiyuan.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author huaishi
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // 保存channel对象


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息： " + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        // 建立连接的时候，执行该函数
        Channel channel = ctx.channel(); // 获取channel对象
        channelGroup.writeAndFlush("【服务器】 - " +
                channel.remoteAddress() + "加入\n"); // 广播消息
        channelGroup.add(channel);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 断开连接的时候，执行该函数
        Channel channel = ctx.channel(); // 获取channel对象
        channelGroup.writeAndFlush("【服务器】 - " +
                channel.remoteAddress() + "离开\n"); // 广播消息

        System.out.println(channelGroup.size());
//        channelGroup.remove(channel); // 写不写无所谓，netty会自动调用
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
