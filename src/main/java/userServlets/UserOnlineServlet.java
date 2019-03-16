package userServlets;

import authentication.Authentication;
import entity.Member;
import entity.User;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/online")
public class UserOnlineServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START USER ONLINE SERVLET IS DONE! (POST)");
        JSONObject jsonObjectResponse = new JSONObject();
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJSon = Utils.getJsonFromRequest(req);

        if (!Authentication.isJuryInDbByCookies(req)) {

            System.out.println(Utils.getCurrentTime() + " / Jury not login in. (POST userServlets.UserOnlineServlet).");

            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Login out");

            //   resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));

        } else {
            System.out.println(Utils.getCurrentTime() + " / Jury '" + userJSon.getString("sId") + "' is ONLINE with Ping.");
            Authentication.ping(userJSon.getString("sId"));
            jsonObjectResponse.append("status", "200");
            jsonObjectResponse.append("message", "ONLINE");


            Member cur=Authentication.getCurrentMemberForEvaluation();
            if(cur!=null){
                jsonObjectResponse.append("memberId",cur.getId())
                        .append("memberName",cur.getLastName()+" "+cur.getFirstName()+" "+cur.getSecondName())
                        .append("category",cur.getCategory().getName());
            }
      /*      for (User element: Authentication.getAllJuryFromDB()
                 ) {
                if(element.getUserName().equals(userJSon.getString("sId"))){
                    if(element.getCurrentMemberForEvaluation()!=null) {
                        jsonObjectResponse.append("memberId", element.getCurrentMemberForEvaluation().getId());
                        jsonObjectResponse.append("memberName", element.getCurrentMemberForEvaluation().getLastName() + element.getCurrentMemberForEvaluation().getFirstName());
                        jsonObjectResponse.append("category", element.getCurrentMemberForEvaluation().getCategory().toString());

                    }
                }
            }

*/

        }

        System.out.println(jsonObjectResponse);
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }
}
