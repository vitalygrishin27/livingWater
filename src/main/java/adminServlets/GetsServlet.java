package adminServlets;

import authentication.Authentication;
import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/admin/gets")
public class GetsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("START GetServlet");
        resp.setContentType("application/json; charset=UTF-8");
        JSONObject jsonObjectResponse = new JSONObject();

        if (req.getParameter("command").equals("getCategory")) {

            for (Category element : Authentication.getRepository().getAllCategoryFromDB()
            ) {
                jsonObjectResponse.append("category", element.getName());
            }
        }

        if (req.getParameter("command").equals("getListOfMembersOnlyNames")) {
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

        if (req.getParameter("command").equals("getListOfJuriesOnlyNames")) {
            for (User element : Authentication.getAllJury()
            ) {
                jsonObjectResponse.append("id", element.getUserName());
                jsonObjectResponse.append("name", element.getLastName() + " " + element.getFirstName() + " " + element.getSecondName());
            }
        }


        if (req.getParameter("command").equals("getMemberInformation")) {
            Member member = Authentication.getRepository().getMemberById(Integer.valueOf(req.getParameter("idMember")));
            for (Field fi : member.getClass().getDeclaredFields()
            ) {
                fi.setAccessible(true);

                try {
                    System.out.println(fi.getName() + "   " + fi.get(member));
                    switch (fi.getName()) {
                        case "address":
                            for (Field addressField : member.getAddress().getClass().getDeclaredFields()
                            ) {
                                addressField.setAccessible(true);
                                if (!addressField.getName().equals("id"))
                                    jsonObjectResponse.append(addressField.getName(), addressField.get(member.getAddress()));
                            }
                            break;
                        case "category":
                            jsonObjectResponse.append("category", member.getCategory().getName());
                            break;
                        case "birth":
                            LocalDateTime ldt = LocalDateTime.ofInstant(member.getBirth().toInstant(), ZoneId.systemDefault());
                            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                            String formatter = formmat1.format(ldt);
                            jsonObjectResponse.append("birth", formatter);
                            break;
                        case "firstSong":
                            jsonObjectResponse.append("firstSong", member.getFirstSong().getName());
                            break;
                        case "secondSong":
                            jsonObjectResponse.append("secondSong", member.getSecondSong().getName());
                            break;
                        default:
                            jsonObjectResponse.append(fi.getName(), fi.get(member));
                            break;
                    }


                } catch (IllegalAccessException e) {
                    System.out.println("Ошибка в преобразовании информации.");
                    e.printStackTrace();
                }

            }


        }


        if (req.getParameter("command").equals("getJuryInformation")) {
            User jury = Authentication.getRepository().getJuryByUserName(req.getParameter("idJury"));
            for (Field fi : jury.getClass().getDeclaredFields()
            ) {
                fi.setAccessible(true);

                try {
                    System.out.println(fi.getName() + "   " + fi.get(jury));
                    jsonObjectResponse.append(fi.getName(), fi.get(jury));
                } catch (IllegalAccessException e) {
                    System.out.println("Ошибка в преобразовании информации жюри.");
                    e.printStackTrace();
                }
            }

        }


        if (req.getParameter("command").equals("getListOfMembersFull")) {
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
                map = new LinkedHashMap<>();
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

// TODO: 22.03.2019
//  окно добавлениеи редактирование категорий


            }


            System.out.println(result);
            resp.getWriter().write(String.valueOf(new JSONArray(result)));
            resp.flushBuffer();
            return;


        }



        if (req.getParameter("command").equals("getListOfMembersOnlyMarkers")) {
            List<Map<String, String>> result = new ArrayList<>();


            for (Member element : Authentication.getRepository().getAllMembersFromDB()
            ) {

                //Добавление в мапу записи о первой песне исполнителя
                Map<String, String> map = new LinkedHashMap<>();
                map.put("id", String.valueOf(element.getId()));
                map.put("category", element.getCategory().getName());
                map.put("songNumber", "1");
                map.put("songName", element.getFirstSong().getName());

                for (User elementJury : Authentication.getAllJury()
                ) {

                  if (Authentication.getRepository().isSongAlreadyEvaluatedByJury(element.getFirstSong(),elementJury)){
                      map.put(elementJury.getUserName(), "+");
                  }else{
                      map.put(elementJury.getUserName(), "0");
                  }

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
                map = new LinkedHashMap<>();
                map.put("id", String.valueOf(element.getId()));
                map.put("category", element.getCategory().getName());
                map.put("songNumber", "2");
                map.put("songName", element.getSecondSong().getName());


                for (User elementJury : Authentication.getAllJury()
                ) {
                    if (Authentication.getRepository().isSongAlreadyEvaluatedByJury(element.getSecondSong(),elementJury)){
                        map.put(elementJury.getUserName(), "+");
                    }else{
                        map.put(elementJury.getUserName(), "0");
                    }
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

            }
            System.out.println(result);
            resp.getWriter().write(String.valueOf(new JSONArray(result)));
            resp.flushBuffer();
            return;
        }


        if (req.getParameter("command").equals("getCurrentMemberDataThatEvaluate")
                && Authentication.getCurrentMemberForEvaluation() != null
                && Authentication.getCurrentSongForEvaluation() != null) {
            resp.setContentType("application/json; charset=UTF-8");
            if (Authentication.getCurrentMemberForEvaluation().getFirstSong().equals(Authentication.getCurrentSongForEvaluation())) {
                jsonObjectResponse.append("songNumber", 1);
            }
            if (Authentication.getCurrentMemberForEvaluation().getSecondSong().equals(Authentication.getCurrentSongForEvaluation())) {
                jsonObjectResponse.append("songNumber", 2);
            }

            jsonObjectResponse.append("memberId", Authentication.getCurrentMemberForEvaluation().getId());

        }


        if (req.getParameter("command").equals("getMarksValueOfMemberThatEvaluate")
                && Authentication.getCurrentMemberForEvaluation() != null
                && Authentication.getCurrentSongForEvaluation() != null) {
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

            System.out.println("SEND " + jsonObjectResponse);

        }


        if (req.getParameter("command").equals("getNextNumberForTurn")) {

            jsonObjectResponse.append("freeTurnNumber", Authentication.getRepository().getFreeTurnNumberFromMemberDB());

        }


        resp.getWriter().write(String.valueOf(jsonObjectResponse));
        resp.flushBuffer();


    }
}