package com.myapp;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RequestListener implements ServletRequestListener {
    private static int requestCount = 0;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        requestCount++;
        System.out.println("Request #" + requestCount + " received.");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // Không cần xử lý gì thêm
    }
}
