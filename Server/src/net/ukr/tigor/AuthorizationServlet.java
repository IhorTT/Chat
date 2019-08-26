package net.ukr.tigor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthorizationServlet extends HttpServlet {

    UserList usersList = UserList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String comand = req.getParameter("command");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String status ="";

        if (comand.equals("login")) {
            status = usersList.login(login, password);
        }else if (comand.equals("logout")){
            status = usersList.logout(login);
        }

        PrintWriter pw = resp.getWriter();
        if (status.equals("OK")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            pw.print(login + " is " + comand);
        } else {
            resp.setStatus(201);
            pw.print("Error: " + status);
        }

    }
}
