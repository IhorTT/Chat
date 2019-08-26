package net.ukr.tigor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUsersStats extends HttpServlet {
    UserList usersList = UserList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String command = req.getParameter("command");

        PrintWriter pw = resp.getWriter();
        resp.setStatus(HttpServletResponse.SC_OK);
        if (command.equals("alluser")) {
            pw.print(usersList.getAllUsersList());
        } else if (command.equals("usersonline")) {
            pw.print(usersList.getOnlineUsersList());
        }


    }
}
