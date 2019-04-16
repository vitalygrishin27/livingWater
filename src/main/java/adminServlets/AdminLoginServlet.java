package adminServlets;

import authentication.Authentication;
import entity.User;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     //   Authentication.log(req.getCookies()[0].getValue() + "  -  AdminLoginServlet (GET)  -- redirect to /admin/login/adminLogin.html.");
        //       System.out.println(Utils.getCurrentTime() + " / START ADMIN SERVLET IS DONE! (GET)");
        req.getRequestDispatcher("/admin/login/adminLogin.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      //  System.out.println(Utils.getCurrentTime() + " / Request received to userServlets.LoginServlet (Jury).");
    //    Authentication.log(req.getCookies()[0].getValue() + "  -  AdminLoginServlet (POST).");
        JSONObject jsonObject = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        User user = Authentication.getRepository().getAdminByUserName(jsonObject.getString("userName"));

        if (user == null) {
            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Invalid ADMIN userName");
            Authentication.log(req.getCookies()[0].getValue() + " / ADMIN with login '" + jsonObject
                             .getString("userName") + "' is missing in DB.");
            //  System.out.println(Utils.getCurrentTime() + " / ADMIN with login '" + jsonObject
           //         .getString("userName") + "' is missing in DB.");
        } else {

            if (jsonObject.getString("password").equals(user.getPassword())) {
                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "You are login in ADMIN!");
                Authentication.log(req.getCookies()[0].getValue() +  " / ADMIN with login '" + jsonObject
                        .getString("userName") + "' is registered.");
            } else {
                jsonObjectResponse.append("status", "ERROR");
                jsonObjectResponse.append("message", "Invalid password ADMIN");
                Authentication.log(req.getCookies()[0].getValue() +  " / ADMIN with login '" + jsonObject
                        .getString("userName") + "' has not correct password.");
            }
        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }
}
