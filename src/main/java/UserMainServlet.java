import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user")
public class UserMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START USER SERVLET IS DONE! (GET)");

        if (Authentication.isAuthenticated(req, "USER")){
            req.getRequestDispatcher("WEB-INF/view/user/mainUser.html").forward(req, resp);

        }else{
            System.out.println("Error with authorization. Redirect to login page.");
            resp.sendRedirect("/");
        }


    }
}
