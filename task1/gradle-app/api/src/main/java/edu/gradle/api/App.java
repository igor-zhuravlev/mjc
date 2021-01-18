package edu.gradle.api;

import edu.gradle.core.Utils;

public class App {

    public static void main(String[] args) {
        boolean ret = Utils.isAllPositiveNumbers("12", "79");
        System.out.println(ret);
    }
}
