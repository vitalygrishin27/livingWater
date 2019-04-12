package userServlets;

import authentication.Authentication;
import entity.MARKCRITERIA;
import entity.Member;
import entity.Song;
import entity.User;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user")
public class UserMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //  System.out.println(Utils.getCurrentTime() + " / START USER SERVLET IS DONE! (GET)");

        if (Authentication.isJuryInDbByCookies(req)) {
            Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (GET)  -  redirect to /user/main/mainUser2.html");
            req.getRequestDispatcher("/user/main/mainUser2.html").forward(req, resp);
        } else {
           // System.out.println(Utils.getCurrentTime() + " / Error with authorization. Redirect to login page. (Jury).");
            Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (GET) -  redirect to / . Authorization error.");
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
     //   System.out.println(Utils.getCurrentTime() + " / Jury - POST DATA.");
        JSONObject jsonObjectResponse = new JSONObject();
        JSONObject userJson = Utils.getJsonFromRequest(req);
        resp.setContentType("application/json; charset=UTF-8");

        if (!Authentication.isJuryInDbByCookies(req)) {
            jsonObjectResponse.append("status", "401");
            jsonObjectResponse.append("message", "Вы неавторизованы.");
          //  System.out.println(Utils.getCurrentTime() + " / Not logged in.");
            Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (POST)  -  redirect to / . Authorization error.");
            resp.sendRedirect("/");
        } else {

            if (messageIsJsonCorrect(userJson).equals("OK")) {
                //Проверка не выставлена ли уже оценка
                if (!Authentication.getRepository().isMemberAlreadyEvaluated(userJson.getString("sId"),
                        Integer.valueOf(userJson.getString("memberId")),
                        Integer.valueOf(userJson.getString("songId")))) {
                    //Занесение оценки в БД
                    Member member = Authentication.getRepository().getMemberById(Integer.valueOf(userJson.getString("memberId")));
                    User jury = Authentication.getRepository().getJuryByUserName(userJson.getString("sId"));
                    Song song = Authentication.getRepository().getSongById(Integer.valueOf(userJson.getString("songId")));
                    // TODO: 18.03.2019 Подумать над критериями
                    Authentication.getRepository().saveMark(member, jury, MARKCRITERIA.VOCAL, song, Integer.valueOf(userJson.getString("vocal")));
                    Authentication.getRepository().saveMark(member, jury, MARKCRITERIA.REPERTOIRE, song, Integer.valueOf(userJson.getString("repertoire")));
                    Authentication.getRepository().saveMark(member, jury, MARKCRITERIA.ARTISTIC, song, Integer.valueOf(userJson.getString("artistic")));
                    Authentication.getRepository().saveMark(member, jury, MARKCRITERIA.INDIVIDUALY, song, Integer.valueOf(userJson.getString("individualy")));

                    Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (POST)  -  Mark is set successful.");
                 //   System.out.println(Utils.getCurrentTime() + " / Mark is set successful.");
                    jsonObjectResponse.append("status", "200");
                    jsonObjectResponse.append("message", "Оценка успешно сохранена.");

                } else {
                 //   System.out.println(Utils.getCurrentTime() + " / Error Mark was already set.");
                    Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (POST)  -  Error Mark was already set.");
                    jsonObjectResponse.append("status", "406");
                    jsonObjectResponse.append("message", "ОШИБКА. Оценка уже была выставлена ранее.");


                }

            } else {
                //System.out.println(Utils.getCurrentTime() + " / Json is not correct.");
                Authentication.log(req.getCookies()[0].getValue() +  "  -  UserMainServlet (POST)  -  Json is not correct.");
                jsonObjectResponse.append("status", "406");
                jsonObjectResponse.append("message", messageIsJsonCorrect(userJson));
            }


        }
        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();
    }

    private String messageIsJsonCorrect(JSONObject json) {
        String resultMessage = "Ошибка. Нет оценки за";

        if (json.getString("vocal").equals("0")) resultMessage += " вокал,";
        if (json.getString("repertoire").equals("0")) resultMessage += " репертуар,";
        if (json.getString("artistic").equals("0")) resultMessage += " артистизм,";
        if (json.getString("individualy").equals("0")) resultMessage += " индивидуальность,";
        if (resultMessage.equals("Ошибка. Нет оценки за")) {
            resultMessage = "";
        } else {
            resultMessage = resultMessage.substring(0, resultMessage.length() - 1) + ".";
        }
        if (json.getString("memberId").equals("undefined")) resultMessage += " + Неверный номер участника.";
        if (resultMessage.length() < 25) resultMessage = "OK";
        return resultMessage;

    }


}
