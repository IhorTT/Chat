package net.ukr.tigor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ChatRoomServlet extends HttpServlet {

    RoomList roomList = RoomList.getInstance();
    UserList usersList = UserList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String comand = req.getParameter("command");
        String login = req.getParameter("login");
        String nameRoom = req.getParameter("room");
        String status ="";
        String comment ="";

        if (comand.equals("add")) {
            status = roomList.addRoom(nameRoom);
            comment = " created";
        }else if (comand.equals("dell")){
            status = roomList.dellRoom(nameRoom);
            comment = " deleted";
        }else if (comand.equals("goroom")){
            status = usersList.changeRoom(login,nameRoom);
            comment = " selected";
        }else if (comand.equals("leaveroom")){
            status = usersList.changeRoom(login,"");
            comment = " left";
        }else if (comand.equals("getList")){
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw = resp.getWriter();
            pw.print(roomList.getRoomsList());
            return;
        }

        PrintWriter pw = resp.getWriter();
        if (status.equals("OK")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            pw.print(nameRoom + comment);
        } else {
            resp.setStatus(201);
            pw.print("Error: " + status);
        }

    }

}
