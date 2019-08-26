package net.ukr.tigor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangeUserStatusServlet extends HttpServlet {
    UserList usersList = UserList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String login = req.getParameter("login");
        String newStatus = req.getParameter("newStatus");
        String status = "";

        status = usersList.changeUserStatus(login, newStatus);

        PrintWriter pw = resp.getWriter();
        if (status.equals("OK")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            pw.print(login + " has changed status by " + newStatus);
        } else {
            resp.setStatus(201);
            pw.print("Error: " + status);
        }
    }
}
