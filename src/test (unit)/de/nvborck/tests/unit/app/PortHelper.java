package de.nvborck.tests.unit.app;

public class PortHelper {

    private static int port = 7777;

    public static int getPort() {
        return port++;
    }
}
