package com.prestigeding.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NioServer {

    private static final int SERVER_PORT = 9080;

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Acceptor(9080));
        thread1.start();
        System.out.println(thread1.getName() + "启动完毕");
        Thread thread2 = new Thread(new Acceptor(9081));
        thread2.start();
    }




}
