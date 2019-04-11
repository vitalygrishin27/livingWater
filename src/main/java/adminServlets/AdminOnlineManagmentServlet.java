package adminServlets;

import authentication.Authentication;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/online")
public class AdminOnlineManagmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            req.getRequestDispatcher("/admin/online/adminOnline.html")
                    .forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
            resp.sendRedirect("/adminLogin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJson = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();

        if (Authentication.isAdminInDbByCookies(req)) {

            if (userJson.getString("command").equals("setMemberForEvaluation")) {

                Authentication.setCurrentMemberForEvaluation(Authentication.getRepository().getMemberById(userJson.getInt("memberId")), Integer.valueOf(userJson.getString("songNumber")));
            }


        } else {
            System.out.println("Access to page AdminOnline (POST) is denided. Authorization error.");
            jsonObjectResponse.append("status", "300");
            jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");
            resp.getWriter().write(String.valueOf(jsonObjectResponse));

            resp.flushBuffer();

        }

    }

}
