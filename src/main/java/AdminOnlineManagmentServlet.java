import entity.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/online")
public class AdminOnlineManagmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START ADMIN ONLINE SERVLET IS DONE! (GET)");
        if (Authentication.isAuthenticated(req, "ADMIN") || Authentication.isAuthenticated(req, "MANAGER")) {
            req.getRequestDispatcher("/WEB-INF/view/admin/managerOnline.html")
               .forward(req, resp);
        } else {
            System.out.println("Not authorization. Return to login page.");
            resp.sendRedirect("/");
            //  return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("START ADMIN ONLINE SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();
        if (Authentication.isAuthenticated(req, "ADMIN") || Authentication.isAuthenticated(req, "MANAGER")) {
            StringBuilder jb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);


                JSONObject userJSon = new JSONObject(jb.toString());


                System.out.println(userJSon);

                if (userJSon.getString("command").equals("getCountOfJuries")) {
                    System.out.println("sID with '" + userJSon.getString("sId") +
                            "' want to get count of juries.");
                    resp.setContentType("application/json; charset=UTF-8");

                    List<User> juryList = Authentication.getJuryList();
                    jsonObjectResponse.append("countOfJuries", juryList.size());
                    System.out.println("There are " + juryList.size() + " of Users in DB. Send to WebSite.");
                    int count = 1;
                    for (User element : juryList
                    ) {
                        jsonObjectResponse.append("jury_" + count + "_name", element.getLastName() + " " + element
                                .getFirstName() + " " + element.getSecondName());
                        jsonObjectResponse.append("jury_" + count +"_office",element.getOffice());
                        count++;
                    }

                    jsonObjectResponse.append("status", "200");
                    jsonObjectResponse.append("message", "Зарегистрированных в БД членов жюри - " + juryList.size());


                    resp.getWriter().write(String.valueOf(jsonObjectResponse));

                    resp.flushBuffer();

                }

            } catch (Exception e) {
                System.out.println("Error with buffered reader.");
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
