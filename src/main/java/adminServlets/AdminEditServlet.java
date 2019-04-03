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
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
                        jsonObjectResponse.append("name", element.getLastName() + " " + element.getFirstName() + " " + element.getSecondName());
                    } else {
                        jsonObjectResponse.append("name", element.getEnsembleName());
                    }

                }
            }
            //Передача всех полей Member через рефлексию.
            if (userJson.getString("command").equals("getMemberInformation")) {
                Member member = Authentication.getRepository().getMemberById(userJson.getInt("idMember"));
                for (Field fi : member.getClass().getDeclaredFields()
                ) {
                    fi.setAccessible(true);

                    try {
                        System.out.println(fi.getName() + "   " + fi.get(member));
                        if (fi.getName().equals("address")) {
                            for (Field addressField : member.getAddress().getClass().getDeclaredFields()
                            ) {
                                addressField.setAccessible(true);
                                if (!addressField.getName().equals("id"))
                                    jsonObjectResponse.append(addressField.getName(), addressField.get(member.getAddress()));
                            }
                        }
                        else if (fi.getName().equals("category")) {
                            jsonObjectResponse.append("category", member.getCategory().getName());
                        }
                        else if (fi.getName().equals("birth")) {
                            LocalDateTime ldt=LocalDateTime.ofInstant(member.getBirth().toInstant(), ZoneId.systemDefault());
                                    DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            String formatter = formmat1.format(ldt);
                            jsonObjectResponse.append("birth", formatter);
                        }
                        else if (fi.getName().equals("firstSong")) {
                            jsonObjectResponse.append("firstSong", member.getFirstSong().getName());
                        }

                        else if (fi.getName().equals("secondSong")) {
                            jsonObjectResponse.append("secondSong", member.getSecondSong().getName());
                        }
                        else{
                            jsonObjectResponse.append(fi.getName(), fi.get(member));
                        }




                    } catch (IllegalAccessException e) {
                        System.out.println("Ошибка в преобразовании информации.");
                        e.printStackTrace();
                    }

                    // fi.getName()

                }


            }

            if (userJson.getString("command").equals("delete")) {
                Authentication.getRepository().deleteMemberFromDBById(userJson.getInt("idMember"));
                jsonObjectResponse.append("status","200").append("message", "Участник успешно удален из БД");

            }

            if (userJson.getString("command").equals("updateSolo")) {
                Member newMember=Utils.getSoloMemberFromJson(userJson);
                Member oldMember=Authentication.getRepository().getMemberById(userJson.getInt("idMember"));
               Authentication.getRepository().updateMember(oldMember,newMember);
                System.out.println("Updating member with "+oldMember.getId()+"("+oldMember.getLastName()+") is successful.");
                jsonObjectResponse.append("status","200").append("message", "Данные участника соло успешно обновлены в БД.");
            }
            if (userJson.getString("command").equals("updateEnsemble")) {
                Member newMember=Utils.getEnsembleFromJson(userJson);
                Member oldMember=Authentication.getRepository().getMemberById(userJson.getInt("idMember"));
                Authentication.getRepository().updateMember(oldMember,newMember);
                System.out.println("Updating member with "+oldMember.getId()+"("+oldMember.getEnsembleName()+") is successful.");
                jsonObjectResponse.append("status","200").append("message", "Данные участника ансамбля успешно обновлены в БД.");
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