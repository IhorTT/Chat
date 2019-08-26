package net.ukr.tigor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static void welcomePage() {
        System.out.println("Welcome to TT chat");
        getCommands();
    }

    private static ArrayList<String> getSysCommands() {
        ArrayList<String> result = new ArrayList<String>();
        result.add("help");
        result.add("reg");
        result.add("login");
        result.add("logout");
        result.add("getusers");
        result.add("getonline");
        result.add("newroom");
        result.add("delroom");
        result.add("goroom");
        result.add("leaveroom");
        result.add("getchatrooms");
        result.add("status");
        return result;
    }

    public static void getCommands() {
        System.out.println("================= system command ======================");
        System.out.println("List of system commands: @help");
        System.out.println("========= autorization/login-out/status command =======");
        System.out.println("Registration -> @reg <nickname> <password>");
        System.out.println("logint to chat -> @login <nickname> <password>");
        System.out.println("logout from chat -> @logout");
        System.out.println("View all registration users -> @getusers");
        System.out.println("View online users -> @getonline");
        System.out.println("Change your status -> @status <online/offline/dnd>");
        System.out.println("================= private message =====================");
        System.out.println("Creat new message to exists user -> @<username>");
        System.out.println("================= chat-room command ===================");
        System.out.println("Creat new chat room -> @newroom <name_of_room>");
        System.out.println("Delete chat room -> @delroom <name_of_room>");
        System.out.println("Enter to chat room -> @goroom <name_of_room>");
        System.out.println("Exit from chat room -> @leaveroom <name_of_room>");
        System.out.println("View all chat rooms -> @getchatrooms");
        System.out.println("=======================================================");
        System.out.println();
    }

    private static Command getCommandWithParams(String userString) {

        ArrayList<String> sysCommands = getSysCommands();
        String commandName = "";
        String[] param = null;

        if (userString.contains("@")) {
            commandName = userString.contains(" ") ? userString.substring(userString.indexOf("@") + 1, userString.indexOf(" "))
                    : userString.substring(userString.indexOf("@") + 1);
            if (!sysCommands.contains(commandName)) {
                param = new String[]{commandName};
                commandName = "sendPrivateMessage";
            } else {
                param = userString.split(" ");
            }
        } else {
            if (Main.login.isEmpty()) {
                System.out.println("You have to login to send message");
                return null;
            } else {
                commandName = "sendPublicMessage";
            }
        }
        return new Command(commandName, param);
    }

    public static void doCommand(String userString) {

        Command command = getCommandWithParams(userString);
        // user is not authorized
        if (command == null) {
            return;
        }
        //System.out.println("Command is :" + command.getName());
        switch (command.getName()) {
            case "sendPublicMessage":
                sendMessage(userString, Main.room, false);
                break;
            case "sendPrivateMessage":
                sendMessage(userString, command.getParam()[0], false);
                break;
            case "help":
                getCommands();
                break;
            case "reg":
                registration(command.getParam());
                break;
            case "login":
                login(command.getParam());
                break;
            case "logout":
                logout();
                break;
            case "getusers":
                getAllUsers();
                break;
            case "getonline":
                getOnlineUsers();
                break;
            case "status":
                setStatus(command.getParam());
                break;
            case "newroom":
                createNewRoom(command.getParam());
                break;
            case "delroom":
                deleteRoom(command.getParam());
                break;
            case "goroom":
                goRoom(command.getParam());
                break;
            case "leaveroom":
                leaveRoom();
                break;
            case "getchatrooms":
                getChatRooms();
                break;
        }
    }

    private static String getBodyMessage(String message) {
        String result = message.substring(message.indexOf(" ", message.indexOf("@")) + 1);
        return result;
    }

    private static void sendMessage(String message, String to, boolean isService) {

        String body = message;
        String from = Main.login;
        if (!isService) {
            body = getBodyMessage(message);
        } else {
            from = "system";
        }

        Message m = new Message(from, to, body, isService);
        try {
            int res = m.send(Utils.getURL() + "/add");
            if (res != 200) {
                System.out.println("HTTP error occured: " + res);
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static void getChatRooms() {
        try {
            URL obj = new URL(Utils.getURL() + "/room?command=getList");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void leaveRoom() {
        boolean success = false;
        String oldRoom = Main.room;
        try {
            URL obj = new URL(Utils.getURL() + "/room?command=leaveroom&login=" + Main.login + "&room=" + Main.room);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.room = "main";
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " left";
            sendMessage(mes, oldRoom, true);
            mes = Main.login + " came";
            sendMessage(mes, Main.room, true);
        }
    }

    private static void goRoom(String[] param) {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/room?command=goroom&login=" + Main.login + "&room=" + param[1]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.room = param[1];
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " came";
            sendMessage(mes, Main.room, true);
        }
    }

    private static void deleteRoom(String[] param) {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/room?command=dell&room=" + param[1]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " deleted room: " + param[1];
            sendMessage(mes, "system", true);
        }
    }

    private static void createNewRoom(String[] param) {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/room?command=add&room=" + param[1]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " created new room: " + param[1];
            sendMessage(mes, "system", true);
        }
    }

    private static void setStatus(String[] param) {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/status?login=" + Main.login + "&newStatus=" + param[1]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.status = param[1];
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " changed status to: " + param[1];
            sendMessage(mes, "system", true);
        }
    }

    private static void getOnlineUsers() {
        try {
            URL obj = new URL(Utils.getURL() + "/stat?command=usersonline");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAllUsers() {
        try {
            URL obj = new URL(Utils.getURL() + "/stat?command=alluser");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMessageFromServer(HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        return strBuf;
    }

    private static void login(String[] param) {
        boolean success = false;
        if (param.length < 3) {
            System.out.println("Error of command");
            return;
        }
        try {
            URL obj = new URL(Utils.getURL() + "/log?command=login&login=" + param[1] + "&password=" + param[2]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.login = param[1];
                Main.room = "main";
                Main.status = "online";
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " entered to chat";
            sendMessage(mes, "system", true);
        }
    }

    private static void logout() {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/log?command=logout&login=" + Main.login);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.login = "";
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " exit from chat";
            sendMessage(mes, "system", true);
        }
    }

    private static void registration(String[] param) {
        if (param.length < 3) {
            System.out.println("Error of command");
            return;
        }
        try {
            URL obj = new URL(Utils.getURL() + "/reg?login=" + param[1] + "&password=" + param[2]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

}

