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

@WebServlet("/admin/registration")
public class AdminTurnNumberServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //     Authentication.log(req.getCookies()[0].getValue() + "  -  .");
        //System.out.println("START ADMIN Edit SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            //    Authentication.log(req.getCookies()[0].getValue() + "  -  redirect to /admin/registration/registration.html");
            req.getRequestDispatcher("/admin/registration/registration.html")
                    .forward(req, resp);

        } else {
            //   Authentication.log(req.getCookies()[0].getValue() + "  -  redirect to / . Error Authorization.");
            resp.sendRedirect("/");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //    System.out.println(Utils.getCurrentTime() + " / START ADMIN Edit SERVLET IS DONE! POST");
        //    Authentication.log(req.getCookies()[0].getValue() + "  -  ");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJSon = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();


        System.out.println(userJSon);
        if (Authentication.isAdminInDbByCookies(req)) {
            if (userJSon.getString("command").equals("setTurnNumberForMember")) {
                Member member = Authentication.getRepository().getMemberById(userJSon.getInt("idMember"));

                Authentication.getRepository().updateTurnNumberForMember(member, userJSon.getInt("turnNumber"));


                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Номер жеребъевки установлен");


            } else {
                //   System.out.println("Access to page AdminEdit (POST) is denided. Authorization error.");
                Authentication.log(req.getCookies()[0].getValue() + "  -  Access to page AdminEdit (POST) is denided. Authorization error.");
                jsonObjectResponse.append("status", "300");
                jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");


            }
            resp.getWriter().write(String.valueOf(jsonObjectResponse));
            resp.flushBuffer();
        }
    }
}