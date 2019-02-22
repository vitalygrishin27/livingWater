import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/manager")
public class ManagerMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START Manager SERVLET IS DONE! (GET)");
        if (Authentication.isAuthenticated(req, "MANAGER")) {
            req.getRequestDispatcher("WEB-INF/view/manager/mainManager.html")
               .forward(req, resp);
        }else{
            System.out.println("Not authorization. Return to login page.");
            resp.sendRedirect("/");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START SERVLET IS DONE! POST");
        resp.setCharacterEncoding("UTF-8");
        //  resp.sendRedirect("/admin");
        //req.getRequestDispatcher("WEB-INF/view/admin.html").forward(req,resp);
        //    doGet(req,resp);
        // super.doPost(req, resp);
    }
}
