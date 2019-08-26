package net.ukr.tigor;

import java.util.Objects;

public class ChatRoom {
    private String name;

    public ChatRoom() {
    }

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(getName(), chatRoom.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
