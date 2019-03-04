import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/admin/jury")
public class AdminAddNewJuryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START ADMIN Add new jury SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            req.getRequestDispatcher("/WEB-INF/view/admin/newJury.html")
               .forward(req, resp);
        } else {
            System.out.println("Not authorization. Return to login page.");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START Admin add new Jury SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();
        if (Authentication.isAdminInDbByCookies(req)) {
            StringBuilder jb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);


                JSONObject userJSon = new JSONObject(jb.toString());


                System.out.println(userJSon);

                if (userJSon.getString("command").equals("addNewJury")) {
                    System.out.println("sID with '" + userJSon.getString("sId") +
                            "' want to add into DB new jury.");
                    resp.setContentType("application/json; charset=UTF-8");

                    String messageToResponse = messageIsJsonCorrect(userJSon);
                    if (messageToResponse.equals("OK")) {
                        //  201 Created («создано»)[2][3][4];
                        System.out.println("Add to DB new JURY is OK.");
                        // TODO: 16.02.2019 Добавление записи жюри в базу данных
                        jsonObjectResponse.append("status", "201");
                        jsonObjectResponse.append("message", "Новый член жюри добавлен в БД.");
                    } else {
                        System.out.println("Add to DB crashed with not filled input boxes");
                        jsonObjectResponse.append("status", "406");
                        jsonObjectResponse.append("message", messageToResponse);
                        //  406 Not Acceptable («неприемлемо»)[2][3];

                    }


                    resp.getWriter().write(String.valueOf(jsonObjectResponse));

                    resp.flushBuffer();

                }

            } catch (Exception e) {
                System.out.println("Error with buffered reader.");
            }


        } else {
            System.out.println("Access to page AdminAddNewJury (POST) is denided. Authorization error.");
            resp.setContentType("application/json; charset=UTF-8");
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
        return resultMessage;


    }


}
