import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user")
public class UserMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START USER SERVLET IS DONE! (GET)");

        if (Authentication.isAuthenticated(req, "USER")) {
            req.getRequestDispatcher("WEB-INF/view/user/mainUser.html").forward(req, resp);

        } else {
            System.out.println("Error with authorization. Redirect to login page.");
            resp.sendRedirect("/");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("USER want to POST DATA");
        JSONObject jsonObjectResponse = new JSONObject();
        JSONObject userJson = Authentication.getJsonFromRequest(req);
        resp.setContentType("application/json; charset=UTF-8");
    //    try {
   //         Thread.sleep(10000);
   //     } catch (InterruptedException e) {
   //         e.printStackTrace();
   //     }


        if (!Authentication.isAuthenticated(req, "USER")) {
            System.out.println("ATTENTION! User want to set MARK, but not authorized.");

            jsonObjectResponse.append("status", "401");
            jsonObjectResponse.append("message", "Вы неавторизованы или Статус подключения - OFF-line.");


        } else {
            if(messageIsJsonCorrect(userJson).equals("OK")){
                // TODO: 27.02.2019 Занесение оценок в БД
                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Оценка успешно сохранена.");

            }
            else{
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

        if (resultMessage.length() < 25) resultMessage = "OK";
        return resultMessage;

    }

}
