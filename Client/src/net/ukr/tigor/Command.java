package net.ukr.tigor;

import java.util.Arrays;

public class Command {
    private String name;
    private String[] param;

    public Command() {
    }

    public Command(String name, String[] param) {
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", param=" + Arrays.toString(param) +
                '}';
    }
}
