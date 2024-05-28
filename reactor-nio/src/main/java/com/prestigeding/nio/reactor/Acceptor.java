package com.prestigeding.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Acceptor implements Runnable {
    private static final int SERVER_PORT = 9080;

    private int port = SERVER_PORT;
    public Acceptor(int port) {
        this.port = port;
    }
    // main Reactor 线程池，用于处理客户端的连接请求
    private static ExecutorService mainReactor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private AtomicInteger num = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("main-reactor-" + num.incrementAndGet());
            System.out.println(t.getName());
            return t;
        }
    });

    public void run() {
        ServerSocketChannel ssc = null;
        try {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName() + "is running");
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(port));

            //转发到 MainReactor反应堆
            //目前只有一个
            dispatch(ssc);
            System.out.println("服务端成功启动。。。。。。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(ServerSocketChannel ssc) {
        //submit 最终会执行start
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "分配了一个mainReactor");
        mainReactor.submit(new MainReactor(ssc));
    }

}
