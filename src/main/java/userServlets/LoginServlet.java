package userServlets;

import authentication.Authentication;
import entity.User;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/juryLogin")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

      //  System.out.println(Utils.getCurrentTime() + " / Request received to userServlets.LoginServlet (Jury).");
        JSONObject jsonObject = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        User user = Authentication.getRepository().getJuryByUserName(jsonObject.getString("userName"));

        if (user == null) {
            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Invalid userName");
            Authentication.log(req.getCookies()[0].getValue() +  " / Jury with login '" + jsonObject
                    .getString("userName") + "' is missing in DB.");
        } else {

            if (jsonObject.getString("password").equals(user.getPassword())) {

                if (!Authentication.isJuryAlreadyInListOnline(user)) {
                    Authentication.addToListOfJuriesOnline(user);
                }
                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "You are login in!");
                Authentication.log(req.getCookies()[0].getValue() +  " / Jury with login '" + jsonObject
                        .getString("userName") + "' is registered.");


            } else {
                jsonObjectResponse.append("status", "ERROR");
                jsonObjectResponse.append("message", "Invalid password");
                Authentication.log(req.getCookies()[0].getValue() + " / Jury with login '" + jsonObject
                        .getString("userName") + "' has not correct password.");
            }
        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();


    }
}
