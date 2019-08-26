package net.ukr.tigor;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    private static final UserList users = new UserList();
    private final List<User> userArray = new ArrayList<User>();
    private final List<User> userOnlineArray = new ArrayList<User>();
    private RoomList roomList = RoomList.getInstance();

    public static UserList getInstance() {
        return users;
    }

    private UserList() {

    }

    private boolean userExists(String login) {
        boolean result = false;
        for (User usr : userArray) {
            if (usr.getLogin().equals(login)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private User getUserOnline(String login) {
        User result = null;
        for (User usr : userOnlineArray) {
            if (usr.getLogin().equals(login)) {
                result = usr;
                break;
            }
        }
        return result;
    }

    public synchronized String registration(String login, String password) {
        String result = "";
        if (userExists(login)) {
            result = "UserAlreadyExists";
        } else {
            userArray.add(new User(login, password));
            result = "OK";
        }
        return result;
    }

    private User getUser(String login, String password) {
        User result = null;
        for (User user : userArray) {
            if (user.getLogin().equals(login) && user.getPass().equals(password)) {
                result = user;
            }
        }
        return result;
    }

    public synchronized String login(String login, String password) {
        String result = "";
        User user = getUser(login, password);
        if (user != null) {
            User userOnlione = getUserOnline(login);
            if (userOnlione != null) {
                result = "User already is online";
            } else {
                user.setStatus("online");
                user.setRoom(roomList.getRoomByName("main"));
                userOnlineArray.add(user);
                result = "OK";
            }
        } else {
            result = "User not found or wrong password!";
        }
        return result;
    }

    public synchronized String logout(String login) {
        String result = "";
        User user = getUserOnline(login);
        if (user != null) {
            user.setStatus("offline");
            user.setRoom(null);
            userOnlineArray.remove(user);
            result = "OK";
        } else {
            result = "User already is offline";
        }

        return result;
    }

    public synchronized String changeRoom(String login, String newRoom) {
        String result = "";

        User userOnlione = getUserOnline(login);
        if (userOnlione == null) {
            return "You can't change the room been offline.";
        }

        if (newRoom.isEmpty()) {
            newRoom = "main";
        }
        ChatRoom room = roomList.getRoomByName(newRoom);
        if (room != null) {
            userOnlione.setRoom(room);
            result = "OK";
        } else {
            result = "Room not found";
        }

        return result;
    }

    public String getAllUsersList() {
        StringBuilder sb = new StringBuilder();
        sb.append("List of users: ").append(System.lineSeparator());
        for (User usr : userArray) {
            sb.append("   " + usr).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String getOnlineUsersList() {
        StringBuilder sb = new StringBuilder();
        sb.append("List of online users: ").append(System.lineSeparator());
        for (User usr : userOnlineArray) {
            sb.append("   " + usr).append(" status: " + usr.getStatus()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String changeUserStatus(String login, String newStatus) {

        String result = "";
        User user = getUserOnline(login);
        if (user != null) {
            user.setStatus(newStatus);
            result = "OK";
        } else {
            result = "You was not able to change the status";
        }
        return result;
    }

    public ArrayList<User> getUserInRoom(ChatRoom room) {
        ArrayList<User> result = new ArrayList<User>();
        for (User usr : userOnlineArray) {
            if (usr.getRoom() == null) {
                continue;
            }
            if (usr.getRoom().equals(room)) {
                result.add(usr);
            }
        }
        return result;
    }

    public synchronized int getCountUserInRoom(ChatRoom room) {

        int result = 0;
        for (User usr : userOnlineArray) {
            if (usr.getRoom() == null) {
                continue;
            }
            if (usr.getRoom().equals(room)) {
                result++;
            }
        }
        return result;
    }
}
