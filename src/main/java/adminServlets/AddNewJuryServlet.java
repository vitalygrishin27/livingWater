package adminServlets;

import authentication.Authentication;
import entity.User;
import org.json.JSONObject;
import repository.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/newJury")
public class AddNewJuryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //   System.out.println("START ADMIN Add new jury SERVLET IS DONE! (GET)");

     //   Authentication.log(req.getCookies()[0].getValue() + "  -  AddNewJuryServlet (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            Authentication.log(req.getCookies()[0].getValue() + "  -  Redirect to newJury.html");
            req.getRequestDispatcher("/admin/newJury/newJury.html")
                    .forward(req, resp);
        } else {
            Authentication.log(req.getCookies()[0].getValue() + "  -  Authorization error.");
            //  System.out.println("Not authorization. Return to login page.");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //   System.out.println(Utils.getCurrentTime() + "/ START Admin add new Jury SERVLET IS DONE! POST");
     //   Authentication.log(req.getCookies()[0].getValue() + "  -  AddNewJuryServlet (POST)");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();

        JSONObject userJSon = Utils.getJsonFromRequest(req);
        Authentication.log(req.getCookies()[0].getValue() + "  -  POST JSON  -  " + userJSon);
        //    System.out.println(userJSon);

        if (userJSon.getString("command").equals("addNewJury")) {
            //   System.out.println("sID with '" + userJSon.getString("sId") +
            //     "' want to add into DB new jury.");
            Authentication.log(req.getCookies()[0].getValue() + "  -  command addNewJury");

            String messageToResponse = messageIsJsonCorrect(userJSon);
            if (messageToResponse.equals("OK")) {

                //проверка на логин
                if (Authentication.getRepository().isLoginForNewJuryCorrect(userJSon.getString("userName"))) {
                    //  201 Created («создано»)[2][3][4];

                    Authentication.getRepository().saveNewJuryIntoDB(Utils.getJuryFromJson(userJSon));
                    //   System.out.println("Add to DB new JURY is OK. (" + userJSon.get("userName") + ")");
                    Authentication.log(req.getCookies()[0].getValue() + "  -  new Jury (" + userJSon.getString("userName") + ") have been created.");
                    jsonObjectResponse.append("status", "201");
                    jsonObjectResponse.append("message", "Новый член жюри добавлен в БД.");


                } else {
                    //     System.out.println(Utils.getCurrentTime() + " / Add to DB new JURY Failed. Bad Login.");
                    Authentication.log(req.getCookies()[0].getValue() + "  -  Jury with login (" + userJSon.getString("userName") + ") can not be create.");
                    jsonObjectResponse.append("status", "406");
                    jsonObjectResponse.append("message", "Недопустимый логин.");
                }
                //   resp.getWriter().write(String.valueOf(jsonObjectResponse));

                //      resp.flushBuffer();


            } else {
                // System.out.println("Add to DB crashed with not filled input boxes");
                Authentication.log(req.getCookies()[0].getValue() + "  -  Add to DB crashed with not filled input boxes.");
                jsonObjectResponse.append("status", "406");
                jsonObjectResponse.append("message", messageToResponse);
                //  406 Not Acceptable («неприемлемо»)[2][3];

            }

        }


        if (userJSon.getString("command").equals("updateJury")) {
            // System.out.println("sID with '" + userJSon.getString("sId") +
            //          "' want to update into DB jury.");
            //     Authentication.log(req.getCookies()[0].getValue() + "  -  update Jury (" + userJSon.getString("idOldJury") + ") to ("+userJSon.getString("userName")+") have been created.");
            String messageToResponse = messageIsJsonCorrect(userJSon);
            if (messageToResponse.equals("OK")) {

                //проверка на логин если он существует значит продолжаем - так как єто обновление
                User oldJury = Authentication.getRepository().getJuryByUserName(userJSon.getString("idOldJury"));
                User newJury = Utils.getJuryFromJson(userJSon);
                // newJury.setUserName(userJSon.getString("userName"));

                Authentication.getRepository().updateJuryInDB(oldJury,newJury);
              //  Authentication.getRepository().saveNewJuryIntoDB(newJury);

                Authentication.log(req.getCookies()[0].getValue() + "  -  update Jury (" + userJSon.getString("idOldJury") + ") to (" + userJSon.getString("userName") + ") is successful.");
                //  System.out.println("Update to DB JURY is OK. (" + userJSon.get("userName") + ")");
                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Информация обновлена.");

            } else {
                //   System.out.println("Update to DB crashed with not filled input boxes");
                Authentication.log(req.getCookies()[0].getValue() + "  -  update Jury (" + userJSon.getString("idOldJury") + ") to (" + userJSon.getString("userName") + ") crashed with not filled input boxes.");
                jsonObjectResponse.append("status", "406");
                jsonObjectResponse.append("message", messageToResponse);
                //  406 Not Acceptable («неприемлемо»)[2][3];

            }

        }


        if (userJSon.getString("command").equals("deleteJury")) {
            Authentication.getRepository().deleteJuryFromDBByUserName(userJSon.getString("idJury"));
            Authentication.log(req.getCookies()[0].getValue() + "  -  deleteJury  -- OK.");
            jsonObjectResponse.append("status", "200").append("message", "Член жюри успешно удален из БД");

        }


        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();




    /*    else {
            System.out.println("Access to page AdminAddNewJury (POST) is denided. Authorization error.");
            resp.setContentType("application/json; charset=UTF-8");
            jsonObjectResponse.append("status", "300");
            jsonObjectResponse.append("message", "Доступ запрещен. Нужна авторизация.");
            resp.getWriter().write(String.valueOf(jsonObjectResponse));

            resp.flushBuffer();

        }*/

    }

    private String messageIsJsonCorrect(JSONObject json) {
        int countNotFilledFields = 0;
        String resultMessage = "OK";
        for (String element : json.keySet()
        ) {
            if (json.getString(element).equals(""))
                resultMessage = "Не заполнено " + (++countNotFilledFields) + " обязательных полей.";
        }
        return resultMessage;


    }


}
