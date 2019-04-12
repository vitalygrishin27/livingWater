package adminServlets;

import authentication.Authentication;
import org.json.JSONObject;
import repository.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/newMember")
public class AddNewMember extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     //   Authentication.log(req.getCookies()[0].getValue() + "  -  AddNewMember (GET)");
        //    System.out.println("START ADMIN Registration member SERVLET IS DONE! (GET)");

        if (Authentication.isAdminInDbByCookies(req)) {
            Authentication.log(req.getCookies()[0].getValue() + "  -  redirect /admin/newMember/createNewMember.html");
            req.getRequestDispatcher("/admin/newMember/createNewMember.html")
                    .forward(req, resp);
        } else {
            Authentication.log(req.getCookies()[0].getValue() + "  -  redirect /. Error authorization.");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //  System.out.println("START Admin registration member SERVLET IS DONE! POST");
    //    Authentication.log(req.getCookies()[0].getValue() + "  -  AddNewMember (POST).");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();
        if (Authentication.isAdminInDbByCookies(req)) {
            JSONObject userJSon = Utils.getJsonFromRequest(req);
            //   System.out.println(userJSon);
            Authentication.log(req.getCookies()[0].getValue() + "  -  receive JSON -  " + userJSon);
            if (userJSon.getString("command").equals("registerSolo")) {
                Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerSolo");
                //  System.out.println("sID with '" + userJSon.getString("sId") +
                //         "' want to add into DB solo member.");
                String messageToResponse = messageIsJsonCorrect(userJSon);
                if (messageToResponse.equals("OK")) {
                    //  201 Created («создано»)[2][3][4];
                    Authentication.getRepository().saveNewMemberIntoDB(Utils.getSoloMemberFromJson(userJSon));
                    Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerSolo  -  Status OK  -  201");
                    //  System.out.println("Add to DB new solo member is OK.");
                    jsonObjectResponse.append("status", "201");
                    jsonObjectResponse.append("message", "Новый солист добавлен в БД.");
                } else {
                    //  System.out.println("Add to DB crashed with not filled input boxes");
                    Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerSolo  -  Add to DB crashed with not filled input boxes.");
                    jsonObjectResponse.append("status", "406");
                    jsonObjectResponse.append("message", messageToResponse);
                    //  406 Not Acceptable («неприемлемо»)[2][3];
                }
            }

            if (userJSon.getString("command").equals("registerEnsemble")) {
                //  System.out.println("sID with '" + userJSon.getString("sId") +
                //             "' want to add into DB ensemble member.");
                Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerEnsemble");
                resp.setContentType("application/json; charset=UTF-8");
                String messageToResponse = messageIsJsonCorrect(userJSon);
                if (messageToResponse.equals("OK")) {
                    Authentication.getRepository().saveNewMemberIntoDB(Utils.getEnsembleFromJson(userJSon));
                    //  201 Created («создано»)[2][3][4];
                    // System.out.println("Add to DB new ensemble member is OK.");
                    Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerEnsemble  -  Status OK  -  201");
                    jsonObjectResponse.append("status", "201");
                    jsonObjectResponse.append("message", "Новый ансамбль добавлен в БД.");
                } else {
                    //   System.out.println("Add to DB crashed with not filled input boxes");
                    Authentication.log(req.getCookies()[0].getValue() + "  -  command -  registerEnsemble  -  Add to DB crashed with not filled input boxes.");
                    jsonObjectResponse.append("status", "406");
                    jsonObjectResponse.append("message", messageToResponse);
                    //  406 Not Acceptable («неприемлемо»)[2][3];
                }
            }
            resp.getWriter().write(String.valueOf(jsonObjectResponse));
            resp.flushBuffer();

        } else {
            //   System.out.println("Access to page AdminAddNewMember (POST) is denided. Authorization error.");

            Authentication.log(req.getCookies()[0].getValue() + "  -  Access to page AdminAddNewMember (POST) is denided. Authorization error.");
            jsonObjectResponse.append("status", "300");
            jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");
            resp.getWriter().write(String.valueOf(jsonObjectResponse));
            resp.flushBuffer();
        }
    }

    private String messageIsJsonCorrect(JSONObject json) {
        int countNotFilledFields = 0;
        String resultMessage = "OK";
        for (String element : json.keySet()
        ) {
            if (json.getString(element).equals(""))
                resultMessage = "Не заполнено " + (++countNotFilledFields) + " обязательных полей.";
        }
        if (json.getString("category").equals("Категория"))
            resultMessage = "Не заполнено " + (++countNotFilledFields) + " обязательных полей.";
        return resultMessage;
    }
}
