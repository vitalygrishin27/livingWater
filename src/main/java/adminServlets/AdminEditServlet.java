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
   //     Authentication.log(req.getCookies()[0].getValue() + "  -  AdminEditServlet (GET).");
        //System.out.println("START ADMIN Edit SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            Authentication.log(req.getCookies()[0].getValue() + "  -  redirect to /admin/edit/edit.html");
            req.getRequestDispatcher("/admin/edit/edit.html")
                    .forward(req, resp);

        } else {
            Authentication.log(req.getCookies()[0].getValue() + "  -  redirect to / . Error Authorization.");
            resp.sendRedirect("/");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //    System.out.println(Utils.getCurrentTime() + " / START ADMIN Edit SERVLET IS DONE! POST");
    //    Authentication.log(req.getCookies()[0].getValue() + "  -  AdminEditServlet (POST).");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject userJson = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();

        if (Authentication.isAdminInDbByCookies(req)) {

            if (userJson.getString("command").equals("deleteMember")) {
                Authentication.getRepository().deleteMemberFromDBById(userJson.getInt("idMember"));
                Authentication.log(req.getCookies()[0].getValue() + "  -  deleteMember  -- OK.");
                jsonObjectResponse.append("status", "200").append("message", "Участник успешно удален из БД");

            }
            if (userJson.getString("command").equals("deleteJury")) {
                Authentication.getRepository().deleteJuryFromDBByUserName(userJson.getString("idJury"));
                Authentication.log(req.getCookies()[0].getValue() + "  -  deleteJury  -- OK.");
                jsonObjectResponse.append("status", "200").append("message", "Член жюри успешно удален из БД");

            }
            if (userJson.getString("command").equals("updateSolo")) {
                Member newMember = Utils.getSoloMemberFromJson(userJson);
                Member oldMember = Authentication.getRepository().getMemberById(userJson.getInt("idMember"));
                Authentication.getRepository().updateMember(oldMember, newMember);
          //      System.out.println("Updating member with " + oldMember.getId() + "(" + oldMember.getLastName() + ") is successful.");
                Authentication.log(req.getCookies()[0].getValue() + "  -  updateSolo  -- OK.");
                jsonObjectResponse.append("status", "200").append("message", "Данные участника соло успешно обновлены в БД.");
            }
            if (userJson.getString("command").equals("updateEnsemble")) {
                Member newMember = Utils.getEnsembleFromJson(userJson);
                Member oldMember = Authentication.getRepository().getMemberById(userJson.getInt("idMember"));
                Authentication.getRepository().updateMember(oldMember, newMember);
              //  System.out.println("Updating member with " + oldMember.getId() + "(" + oldMember.getEnsembleName() + ") is successful.");
                Authentication.log(req.getCookies()[0].getValue() + "  -  updateEnsemble  -- OK.");
                jsonObjectResponse.append("status", "200").append("message", "Данные участника ансамбля успешно обновлены в БД.");
            }




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