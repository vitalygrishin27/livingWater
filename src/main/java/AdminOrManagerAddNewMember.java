import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/admin/registration/member")
public class AdminOrManagerAddNewMember extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START ADMIN Registration member SERVLET IS DONE! (GET)");

        if (Authentication.isAuthenticated(req, "ADMIN") || Authentication.isAuthenticated(req, "MANAGER")) {
            req.getRequestDispatcher("/WEB-INF/view/admin/registration/member/registrationMember.html")
               .forward(req, resp);

        } else {
            resp.sendRedirect("/");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("START Admin registration member SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();
        if (Authentication.isAuthenticated(req, "ADMIN") || Authentication.isAuthenticated(req, "MANAGER")) {

          //  System.out.println("USER Want to log in.");
            StringBuilder jb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);


                JSONObject userJSon = new JSONObject(jb.toString());


                System.out.println(userJSon);

                if (userJSon.getString("command").equals("registerSolo")) {
                    System.out.println("sID with '" + userJSon.getString("sId") +
                            "' want to add into DB solo member.");
                    resp.setContentType("application/json; charset=UTF-8");

                    String messageToResponse = messageIsJsonCorrect(userJSon);
                    if (messageToResponse.equals("OK")) {
                        //  201 Created («создано»)[2][3][4];
                        System.out.println("Add to DB new solo member is OK.");
                        // TODO: 16.02.2019 Добавление записи в базу данных
                        jsonObjectResponse.append("status", "201");
                        jsonObjectResponse.append("message", "Новый солист добавлен в БД.");
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
            System.out.println("Access to page AdminAddNewMember (POST) is denided. Authorization error.");
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

        if (json.getString("category").equals("Категория"))
            resultMessage = "Не заполнено " + (++countNotFilledFields) + " обязательных полей.";


        return resultMessage;


    }
}
