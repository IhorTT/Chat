package net.ukr.tigor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {

    UserList usersList = UserList.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String status ="";

        status = usersList.registration(login, password);
        PrintWriter pw = resp.getWriter();

        if (status.equals("OK")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            pw.print("Registration completed. Please, login to chat with your nickname");
        } else {
            resp.setStatus(201);
            pw.print("Error:" + status);
        }
    }
}
