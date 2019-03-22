package adminServlets;

import authentication.Authentication;
import entity.Member;
import org.json.JSONObject;
import repository.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/edit")
public class AdminEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START ADMIN Registration member SERVLET IS DONE! (GET)");

        if (Authentication.isAdminInDbByCookies(req)) {
            req.getRequestDispatcher("/admin/edit/edit.html")
                    .forward(req, resp);

        } else {
            resp.sendRedirect("/");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJson = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();

        if (Authentication.isAdminInDbByCookies(req)) {
            if (userJson.getString("command").equals("getListOfMembers")) {
                for (Member element : Authentication.getRepository().getAllMembersFromDB()
                ) {
                    jsonObjectResponse.append("id", element.getId());
                    if (element.getEnsembleName().equals("")) {
                        jsonObjectResponse.append("name", element.getLastName());
                    } else {
                        jsonObjectResponse.append("name", element.getEnsembleName());
                    }

                }
            }




        } else {
            System.out.println("Access to page AdminEdit (POST) is denided. Authorization error.");
            jsonObjectResponse.append("status", "300");
            jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");


        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }
}