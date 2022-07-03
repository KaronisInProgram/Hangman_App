package de.nvborck.tests.app;

public class PortHelper {

    private static int port = 7777;

    public static int getPort() {
        return port++;
    }
}
