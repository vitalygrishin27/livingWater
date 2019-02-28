import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/user/online")
public class UserOnlineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START USER ONLINE SERVLET IS DONE! (GET)");

  /*

   if (Authentication.isAuthenticated(req, "USER")){
            req.getRequestDispatcher("WEB-INF/view/user/mainUser.html").forward(req, resp);

        }else{
            System.out.println("Error with authorization. Redirect to login page.");
            resp.sendRedirect("/");
        }

*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START USER ONLINE SERVLET IS DONE! (POST)");
       if(!Authentication.isAuthenticated(req,"USER")){
           System.out.println("ATTENTION! User will be added to sIds after server error.");
        Authentication.addSIdToMap(req);
       }
        req.setCharacterEncoding("UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();

        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);


            JSONObject userJSon = new JSONObject(jb.toString());
            if (userJSon.getString("command").equals("online")) {
                System.out.println("sID with '" + userJSon.getString("sId") +
                        "' is ONLINE. "+ Authentication.getCurrentTime());
                resp.setContentType("application/json; charset=UTF-8");

                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "ON-LINE");

                resp.getWriter().write(String.valueOf(jsonObjectResponse));

                resp.flushBuffer();

            }

        } catch (Exception e) {
            System.out.println("Error with buffered reader.");
        }

    }
}
