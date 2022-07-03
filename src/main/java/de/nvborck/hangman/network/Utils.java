package de.nvborck.hangman.network;

import java.util.Hashtable;
import java.util.Map;

public class Utils {

    public static final String extra_request = "request";
    public static final String extra_answer = "answer";

    public static final String option = "option";
    public static final String id = "id";
    public static final String extra = "extra";

    public static Map<String, String> splitUri(String uri) {
        String[] splits = uri.split("/");

        Map<String, String> result = new Hashtable<>();
        if(splits.length > 0) {
            result.put(option, splits[0]);
        }
        if(splits.length > 1) {
            result.put(id, splits[1]);
        }
        int offset = 1;
        for (int i = 2; i < splits.length; i++) {
            result.put(extra + (i-offset), splits[i]);
        }

        return result;
    }

    public static void log(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>>>>>>>>>>>> YOUR APPLICATION | YOUR APPLICATION | YOUR APPLICATION <<<<<<<<<<<<<<<<<<<<<\n");
        sb.append(msg);
        sb.append("\n>>>>>>>>>>>>>> YOUR APPLICATION | YOUR APPLICATION | YOUR APPLICATION <<<<<<<<<<<<<<<<<<<<<\n");
        System.out.println(sb);
    }
}
