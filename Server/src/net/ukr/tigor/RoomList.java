package net.ukr.tigor;

import java.util.ArrayList;
import java.util.List;

public class RoomList {

    private static final RoomList romms = new RoomList();
    private final List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();


    public static RoomList getInstance() {
        return romms;
    }

    private RoomList() {
        chatRooms.add(new ChatRoom("main"));
    }

    public ChatRoom getRoomByName(String name) {
        ChatRoom room = null;
        for (ChatRoom chr : chatRooms) {
            if (chr.getName().equals(name)) {
                room = chr;
                break;
            }
        }
        return room;
    }

    public synchronized String addRoom(String name) {
        String result = "";
        ChatRoom room = getRoomByName(name);
        if (room != null) {
            result = "Room already exists";
        } else {
            chatRooms.add(new ChatRoom(name));
            result = "OK";
        }
        return result;
    }

    public synchronized String dellRoom(String name) {
        String result = "";
        if (name.equals("main")) {
            return "You can't delete main room!";
        }
        ChatRoom room = getRoomByName(name);
        if (room != null) {
            int usersInRoom = UserList.getInstance().getCountUserInRoom(room);
            if (usersInRoom == 0) {
                chatRooms.remove(room);
                result = "OK";
            } else {
                result = "You can't delete the used room!";
            }
        } else {
            result = "Room doesn't exist";
        }
        return result;
    }

    public synchronized String getRoomsList() {
        StringBuilder sb = new StringBuilder();
        sb.append("List of rooms: ").append(System.lineSeparator());
        for (ChatRoom chr : chatRooms) {
            int size = UserList.getInstance().getCountUserInRoom(chr);
            sb.append("   " + chr).append("(" + size + ")").append(System.lineSeparator());
        }
        return sb.toString();
    }

}
