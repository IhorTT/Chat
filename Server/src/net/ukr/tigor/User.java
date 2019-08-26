package net.ukr.tigor;

import java.util.Objects;

public class User {

    private String login;
    private String pass;
    private String status;
    private ChatRoom room;

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPass(), user.getPass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPass());
    }

    @Override
    public String toString() {
        return login;
    }
}
