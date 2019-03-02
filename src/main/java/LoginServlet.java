
import entity.User;
import logical.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / Request received to LoginServlet (Jury).");
        JSONObject jsonObject = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        User user = Authentication.getRepository().getUserByUserName(jsonObject.getString("userName"));


        if (user == null) {
            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Invalid userName");
            //  resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));
            System.out.println(Utils.getCurrentTime() + " / Jury with login '" + jsonObject
                    .getString("userName") + "' is missing in DB.");
        } else {

            if (jsonObject.getString("password").equals(user.getPassword())) {
                if (!Authentication.isJuryOnline(user)) {
                    Authentication.addToListOfJuriesOnline(user);
                }
                //  jsonObjectResponse.append("status", "ERROR");
                    //   jsonObjectResponse.append("message", "The same user already connected to server.");
                    //      resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));
                    jsonObjectResponse.append("status", "200");
                    jsonObjectResponse.append("message", "You are login in!");
                    //      resp.addCookie(new Cookie("LivingWaterSession", user.getUserName()));
                    //Authentication.addToListOfJuriesOnline(user);
                    System.out.println(Utils.getCurrentTime() + " / Jury with login '" + jsonObject
                            .getString("userName") + "' is registered.");



            } else {
                jsonObjectResponse.append("status", "ERROR");
                jsonObjectResponse.append("message", "Invalid password");
                //     resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));
                System.out.println(Utils.getCurrentTime() + " / Jury with login '" + jsonObject
                        .getString("userName") + "' has not correct password.");
            }


        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();


    }
}
