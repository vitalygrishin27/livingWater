import logical.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user")
public class UserMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START USER SERVLET IS DONE! (GET)");

        if (Authentication.isJuryInDbByCookies(req)) {
            req.getRequestDispatcher("WEB-INF/view/user/mainUser.html").forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Error with authorization. Redirect to login page. (Jury).");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / Jury - POST DATA.");
        JSONObject jsonObjectResponse = new JSONObject();
        JSONObject userJson =Utils.getJsonFromRequest(req);
        resp.setContentType("application/json; charset=UTF-8");

        if(!Authentication.isJuryInDbByCookies(req)){
            jsonObjectResponse.append("status", "401");
            jsonObjectResponse.append("message", "Вы неавторизованы.");
            System.out.println(Utils.getCurrentTime() + " / Not logged in.");
            resp.sendRedirect("/");
        }
        else{

            if (messageIsJsonCorrect(userJson).equals("OK")) {


                // TODO: 27.02.2019 Занесение оценок в БД
                System.out.println(Utils.getCurrentTime() +  " / Mark is set successful.");
                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Оценка успешно сохранена.");

            } else {
                System.out.println(Utils.getCurrentTime() + " / Json is not correct.");
                jsonObjectResponse.append("status", "406");
                jsonObjectResponse.append("message", messageIsJsonCorrect(userJson));
            }


        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }

    private String messageIsJsonCorrect(JSONObject json) {
        String resultMessage = "Ошибка. Нет оценки за";

        if (json.getString("vocal").equals("0")) resultMessage += " вокал,";
        if (json.getString("repertoire").equals("0")) resultMessage += " репертуар,";
        if (json.getString("artistic").equals("0")) resultMessage += " артистизм,";
        if (json.getString("individualy").equals("0")) resultMessage += " индивидуальность,";
        resultMessage=resultMessage.substring(0,resultMessage.length()-2)+".";
        if (json.getString("memberId").equals("undefined")) resultMessage+= " + Неверный номер участника.";

        if (resultMessage.length() < 25) resultMessage = "OK";
        return resultMessage;

    }

    private boolean verifyMemberData(){
        return true;
    }

}
