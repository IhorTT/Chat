package net.ukr.tigor;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String login = "";
    public static String room = "";
    public static String status = "";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Thread th = new Thread(new GetThread());
        th.setDaemon(true);
        th.start();

        Utils.welcomePage();

        if (login.isEmpty()) {
            System.out.println("Please, register or login to start chatting");
            System.out.println(">");
        }

        // test
        //Utils.doCommand("@reg user1 1");
        //Utils.doCommand("@login user1 1");
        // test

        while (true) {
            System.out.println(getPrompt());
            String text = scanner.nextLine();
            if (text.equals("exit")) {
                Utils.doCommand("@logout");
                break;
            }
            Utils.doCommand(text);
        }
        scanner.close();
    }

    private static String getPrompt() {
        String result = "";
        if (!login.isEmpty()) {
            result = "[" + room + "] " + login + " is " + status + " >>";
        } else {
            result = ">";
        }
        return result;
    }
}
