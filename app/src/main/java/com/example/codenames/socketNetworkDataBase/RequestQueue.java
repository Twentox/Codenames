package com.example.codenames.socketNetworkDataBase;

import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public abstract class RequestQueue {
    private static final LinkedList<SocketRequest> list = new LinkedList<>();

    public static class SocketRequest {
        private static Socket socket;
        private static String data = null;

        public SocketRequest(Socket socket, String data) {
            if (socket == null) throw new IllegalArgumentException("[>] Invalid socket");
            if (data == null) throw new IllegalArgumentException("[>] Invalid data");

            this.socket = socket;
            this.data = data;
        }

        public Socket getSocket() {
            return socket;
        }

        public static String getData() {
            return data;
        }
    }

    public static synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public static synchronized void enqueue(SocketRequest item) {
        list.addLast(item);
    }

    public static synchronized SocketRequest dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeue from an empty list");
        }
        return list.removeFirst();
    }

    public static synchronized SocketRequest peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Peek from an empty list");
        }
        return list.getFirst();
    }

    public static synchronized int size() {
        return list.size();
    }
}
