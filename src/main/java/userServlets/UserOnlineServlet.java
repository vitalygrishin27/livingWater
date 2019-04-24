package userServlets;

import authentication.Authentication;
import entity.Member;
import entity.Song;
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
        //   System.out.println(Utils.getCurrentTime() + " / START USER ONLINE SERVLET IS DONE! (POST)");
        //    Authentication.log(req.getCookies()[0].getValue() + "  -  START USER ONLINE SERVLET.");
        JSONObject jsonObjectResponse = new JSONObject();
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJSon = Utils.getJsonFromRequest(req);

        if (!Authentication.isJuryInDbByCookies(req)) {

            //     System.out.println(Utils.getCurrentTime() + " / Jury not login in. (POST userServlets.UserOnlineServlet).");
            Authentication.log(req.getCookies()[0].getValue() + "  -  UserOnlineServlet (POST)  -  Jury not login in.");
            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Login out");

        } else {
            Authentication.log(req.getCookies()[0].getValue() + " / Jury '" + userJSon.getString("sId") + "' is ONLINE with Ping.");
            Authentication.ping(userJSon.getString("sId"));
            jsonObjectResponse.append("status", "200");
            jsonObjectResponse.append("message", "ONLINE");


            Member curMem = Authentication.getCurrentMemberForEvaluation();
            Song curSong = Authentication.getCurrentSongForEvaluation();
            if (curMem != null) {
                jsonObjectResponse.append("memberId", curMem.getId())
                        //     .append("memberName", curMem.getLastName() + " " + curMem.getFirstName() + " " + curMem.getSecondName())
                        .append("category", curMem.getCategory().getName())
                        .append("songName", curSong.getName())
                        .append("songId", curSong.getId())
                        .append("office", curMem.getOffice());
                if (curMem.getEnsembleName().equals("")) {
                    jsonObjectResponse.append("memberName", curMem.getLastName() + " " + curMem.getFirstName() + " " + curMem.getSecondName());
                } else {
                    jsonObjectResponse.append("memberName", curMem.getEnsembleName());
                }

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
        Authentication.log(req.getCookies()[0].getValue() + " --  SEND to JURY " + jsonObjectResponse);
     //   System.out.println("SEND to JURY " + jsonObjectResponse);
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }
}
