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
import java.util.List;

@WebServlet("/admin/online")
public class AdminOnlineManagmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            req.getRequestDispatcher("/WEB-INF/view/admin/managerOnline.html")
                    .forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        JSONObject userJson = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();
      //  System.out.println(userJson);

        if (Authentication.isAdminInDbByCookies(req)) {
            if (userJson.getString("command").equals("getCountOfJuries")) {
                System.out.println("Admin '" + userJson.getString("sId") +
                        "' want to get count of juries.");
                resp.setContentType("application/json; charset=UTF-8");

                List<User> juryList = Authentication.getAllJuryFromDB();

                jsonObjectResponse.append("countOfJuries", juryList.size());
                System.out.println("There are " + juryList.size() + " of Users in DB. Send to WebSite.");
                int count = 1;
                for (User element : juryList
                ) {
                    jsonObjectResponse.append("jury_" + count + "_name", element.getLastName() + " " + element
                            .getFirstName() + " " + element.getSecondName());
                    jsonObjectResponse.append("jury_" + count + "_office", element.getOffice());
                    jsonObjectResponse.append("jury_"+count+"_ping", Authentication.getSecondsAfterPingJury(element.getUserName()));
                    count++;
                }

                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Зарегистрированных в БД членов жюри - " + juryList.size());

                resp.getWriter().write(String.valueOf(jsonObjectResponse));
                resp.flushBuffer();
            }

        } else {
            System.out.println("Access to page AdminOnline (POST) is denided. Authorization error.");
            resp.setContentType("application/json; charset=UTF-8");
            jsonObjectResponse.append("status", "300");
            jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");
            resp.getWriter().write(String.valueOf(jsonObjectResponse));

            resp.flushBuffer();

        }

    }

}
