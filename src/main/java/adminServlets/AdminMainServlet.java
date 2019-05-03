package adminServlets;

import authentication.Authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class AdminMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //  System.out.println(Utils.getCurrentTime() + " / START ADMIN SERVLET IS DONE! (GET)");

        if (Authentication.isAdminInDbByCookies(req)) {
        //    Authentication.log(req.getCookies()[0].getValue() + "  -  AdminMainServlet (GET)  -  redirect to /admin/mainMenu/mainAdmin.html");
            req.getRequestDispatcher("/admin/mainMenu/mainAdmin.html")
                    .forward(req, resp);
        } else {
           // System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
        //    Authentication.log("AdminMainServlet (GET)  -  redirect to /adminLogin. Authorization error.");
            resp.sendRedirect("/adminLogin");
        }
    }

}
