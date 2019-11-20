package com.xzq;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class FastThreadLocalTest {
	
	private static FastThreadLocal<Integer> ft = new FastThreadLocal<Integer>();

	public static void main(String[] args) {
		
		new FastThreadLocalThread(() -> {
			for (int i = 0; i < 100; i++) {
				ft.set(i);
                System.out.println(Thread.currentThread().getName() + "====" + ft.get());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
		}, "fastthreadlocal").start();

		new FastThreadLocalThread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "====" + ft.get());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "fastThreadLocal2").start();
	}

}
