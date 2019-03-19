package adminServlets;

import authentication.Authentication;
import entity.Mark;
import entity.Member;
import entity.Song;
import entity.User;
import org.json.JSONArray;
import repository.Utils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/online")
public class AdminOnlineManagmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {
            req.getRequestDispatcher("/admin/online/adminOnline.html")
                    .forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
            resp.sendRedirect("/adminLogin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN ONLINE SERVLET IS DONE! POST");
        req.setCharacterEncoding("UTF-8");
        JSONObject userJson = Utils.getJsonFromRequest(req);
        JSONObject jsonObjectResponse = new JSONObject();
        //  System.out.println(userJson);

        if (Authentication.isAdminInDbByCookies(req)) {
      /*      if (userJson.getString("command").equals("getCountOfJuries")) {
                System.out.println("Admin '" + userJson.getString("sId") +
                        "' want to get count of juries.");
                resp.setContentType("application/json; charset=UTF-8");

                List<User> juryList = Authentication.getAllJuryFromDB();

                jsonObjectResponse.append("countOfJuries", juryList.size());
                System.out.println("There are " + juryList.size() + " of Users in DB. Send to WebSite.");
                int count = 1;
                for (User element : juryList
                ) {
                    jsonObjectResponse.append("jury_" + count + "_name", element.getLastName() + " " + element
                            .getFirstName() + " " + element.getSecondName());
                    jsonObjectResponse.append("jury_" + count + "_office", element.getOffice());
                    jsonObjectResponse.append("jury_" + count + "_ping", Authentication.getSecondsAfterPingJury(element.getUserName()));
                    count++;
                }

                jsonObjectResponse.append("status", "200");
                jsonObjectResponse.append("message", "Зарегистрированных в БД членов жюри - " + juryList.size());

                resp.getWriter().write(String.valueOf(jsonObjectResponse));
                resp.flushBuffer();
            }*/
            if (userJson.getString("command").equals("getListOfMembers")) {
                resp.setContentType("application/json; charset=UTF-8");
                List<Map<String, String>> result = new ArrayList<>();


                for (Member element : Authentication.getRepository().getAllMembersFromDB()
                ) {

                    //Добавление в мапу записи о первой песне исполнителя
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("id", String.valueOf(element.getId()));
                    map.put("category", element.getCategory().getName());
                    map.put("songNumber", "1");
                    map.put("songName", element.getFirstSong().getName());

                    int summaryMarkValue = 0;
                    //считает количество оценок, должно быть равно количеству критериев оценивая
                    int flagToControleFullMarks = 0;
                    for (User elementJury : Authentication.getAllJury()
                    ) {
                        for (Mark elementMark : Authentication.getRepository().getAllMarksFromDB()
                        ) {
                            if (elementMark.getJury().equals(elementJury)) {
                                if (elementMark.getSong().equals(element.getFirstSong())) {
                                    System.out.println("Контроль суммарной оценки." + elementMark.getValue());
                                    flagToControleFullMarks++;
                                    summaryMarkValue += elementMark.getValue();
                                }


                            }


                        }
                        System.out.println("Full mark consists in " + flagToControleFullMarks);
                        map.put(elementJury.getUserName(), String.valueOf(summaryMarkValue));
                        summaryMarkValue = 0;
                        flagToControleFullMarks = 0;
                    }


                    //Если участник солист
                    if (element.getCountOfMembers() == 1) {
                        map.put("name", element.getLastName() + " " + element.getFirstName() + " " + element.getSecondName());


                    }
                    //Если участник ансамбль
                    else {
                        map.put("name", element.getEnsembleName());

                    }
                    result.add(map);

                    //Добавление в мапу записи о второй песне исполнителя
                    map = new LinkedHashMap<String, String>();
                    map.put("id", String.valueOf(element.getId()));
                    map.put("category", element.getCategory().getName());
                    map.put("songNumber", "2");
                    map.put("songName", element.getSecondSong().getName());

                    summaryMarkValue = 0;
                    //считает количество оценок, должно быть равно количеству критериев оценивая
                    flagToControleFullMarks = 0;
                    for (User elementJury : Authentication.getAllJury()
                    ) {
                        for (Mark elementMark : Authentication.getRepository().getAllMarksFromDB()
                        ) {
                            if (elementMark.getJury().equals(elementJury)) {
                                if (elementMark.getSong().equals(element.getSecondSong())) {
                                    System.out.println("Контроль суммарной оценки." + elementMark.getValue());
                                    flagToControleFullMarks++;
                                    summaryMarkValue += elementMark.getValue();
                                }


                            }


                        }
                        System.out.println("Full mark consists in " + flagToControleFullMarks);
                        map.put(elementJury.getUserName(), String.valueOf(summaryMarkValue));
                        summaryMarkValue = 0;
                        flagToControleFullMarks = 0;
                    }


                    //Если участник солист
                    if (element.getCountOfMembers() == 1) {
                        map.put("name", element.getLastName() + " " + element.getFirstName() + " " + element.getSecondName());


                    }
                    //Если участник ансамбль
                    else {
                        map.put("name", element.getEnsembleName());

                    }
                    result.add(map);

// TODO: 16.03.2019 Сделать у юзера номер песни, название песни. После оценивания чтобы затенялся экран в ожидании, чтобы выходило на экран логина, коргда у юзера логаут, 

                }


                //






           /*     for (int i = 0; i < 5; i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", String.valueOf(i));
                    map.put("pass", i + String.valueOf(i));
                    result.add(map);
                }

                JSONArray jsonObjectResponse2 = new JSONArray();*/
          /*      for (Map<String,String> element:result
                     ) {
                    for (Map.Entry<String,String> entry:element.entrySet()
                         ) {
                        String key=entry.getKey();
                        String value=entry.getValue();
                        jsonObjectResponse2.p


                    }



                }
                */

                //     jsonObjectResponse2.put(result);



          /*
                Map<String,String> map=new HashMap<>();
                map.put("name", String.valueOf(8678));
                map.put("pass", 6787 +String.valueOf(657657657));




                JSONObject jsonObjectResponse2=new JSONObject(map);

                jsonObjectResponse2.a
                System.out.println(jsonObjectResponse2);
               // jsonObjectResponse2.to
*/
                JSONObject jsonObjectResponse2 = new JSONObject();


                //  jsonObjectResponse2.append();
                //    System.out.println(jsonObjectResponse2);


                System.out.println(result);
                resp.getWriter().write(String.valueOf(new JSONArray(result)));
                //     resp.getWriter().write(String.valueOf(jsonObjectResponse2));
                resp.flushBuffer();


            }


            if (userJson.getString("command").equals("setMemberForEvaluation")) {
                resp.setContentType("application/json; charset=UTF-8");
                Authentication.setCurrentMemberForEvaluation(Authentication.getRepository().getMemberById(userJson.getInt("memberId")), Integer.valueOf(userJson.getString("songNumber")));

                //   Authentication.setCurrentSongForEvaluation(Authentication.getRepository().);
            }

            if (userJson.getString("command").equals("getMarksValueOfMemberThatEvaluate")
                    && Authentication.getCurrentMemberForEvaluation() != null
                    && Authentication.getCurrentSongForEvaluation() != null) {
                resp.setContentType("application/json; charset=UTF-8");
                Member member = Authentication.getCurrentMemberForEvaluation();
                Song song = Authentication.getCurrentSongForEvaluation();


                Map<String, Integer> markByJury = new HashMap<>();
                for (User userElement : Authentication.getAllJury()
                ) {
                    markByJury.put(userElement.getUserName(), 0);
                }


                for (Mark element : Authentication.getRepository().getListOfMarksBySong(song)
                ) {
                    markByJury.put(element.getJury().getUserName(), markByJury.get(element.getJury().getUserName()) + element.getValue());

                }

                markByJury.put("memberId", member.getId());
                if (song.equals(member.getFirstSong())) markByJury.put("songNumber", 1);
                if (song.equals(member.getSecondSong())) markByJury.put("songNumber", 2);

                for (Map.Entry<String, Integer> element : markByJury.entrySet()
                ) {
                    jsonObjectResponse.append(element.getKey(), element.getValue());
                }

           //     jsonObjectResponse.append("juryUserName",userJson.getString("sId"));
                System.out.println("SEND "+jsonObjectResponse);
                resp.getWriter().write(String.valueOf(jsonObjectResponse));
             //   resp.getWriter().write(String.valueOf(new JSONArray(new1)));
                resp.flushBuffer();

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

}
