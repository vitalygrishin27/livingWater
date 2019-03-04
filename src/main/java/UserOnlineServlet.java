import logical.Utils;
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

            System.out.println(Utils.getCurrentTime() + " / Jury not login in. (POST UserOnlineServlet).");

            jsonObjectResponse.append("status", "ERROR");
            jsonObjectResponse.append("message", "Login out");
         //   resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));

        } else {
            System.out.println(Utils.getCurrentTime() + " / Jury '" + userJSon.getString("sId") + "' is ONLINE. ");
            jsonObjectResponse.append("status", "200");
            jsonObjectResponse.append("message", "ONLINE");
        }

        System.out.println(jsonObjectResponse);
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }
}
