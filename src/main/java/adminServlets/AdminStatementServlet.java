package adminServlets;

import authentication.Authentication;
import entity.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import repository.Utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static repository.Utils.copyRow;


@WebServlet("/admin/statement")
public class AdminStatementServlet extends HttpServlet {

    static int SUMMARYCOLUMNINDEX = 34;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN STATEMENT SERVLET IS DONE! (POST)");
        if (Authentication.isAdminInDbByCookies(req)) {
            POIFSFileSystem fs = new POIFSFileSystem((new FileInputStream("statement.xls")));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sourceSheet = wb.getSheetAt(0);
            int indexOfSheet = 1;
            List<Integer> summaryMarksListForPlace = new ArrayList<>();

            //Для всех категорий
            for (Category categoryElement : Authentication.getRepository().getAllCategoryFromDB()
            ) {

                //Ориентация и поля листа для печати
                wb.createSheet(categoryElement.getName());
                wb.getSheetAt(indexOfSheet).getPrintSetup().setLandscape(true);
                wb.getSheetAt(indexOfSheet).getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                wb.getSheetAt(indexOfSheet).setMargin(Sheet.LeftMargin, wb.getSheetAt(0).getMargin(Sheet.LeftMargin));
                wb.getSheetAt(indexOfSheet).setMargin(Sheet.RightMargin, wb.getSheetAt(0).getMargin(Sheet.RightMargin));
                wb.getSheetAt(indexOfSheet).setMargin(Sheet.TopMargin, wb.getSheetAt(0).getMargin(Sheet.TopMargin));
                wb.getSheetAt(indexOfSheet).setMargin(Sheet.BottomMargin, wb.getSheetAt(0).getMargin(Sheet.BottomMargin));


                copyRow(wb, sourceSheet, wb.getSheetAt(indexOfSheet), 0, 0);
                copyRow(wb, sourceSheet, wb.getSheetAt(indexOfSheet), 1, 1);
                copyRow(wb, sourceSheet, wb.getSheetAt(indexOfSheet), 2, 2);
                copyRow(wb, sourceSheet, wb.getSheetAt(indexOfSheet), 3, 3);

                HSSFSheet sheet = wb.getSheetAt(indexOfSheet);
                //  HSSFSheet sheet=wb.createSheet();
                //Название категории
                HSSFRow row = sheet.getRow(2);
                //HSSFCell cell = row.getCell(3);

                HSSFCell cell = row.getCell(3);
                cell.setCellValue(categoryElement.getName());

                //Количество участников в категории
                row = sheet.getRow(2);
                cell = row.getCell(26);

                List<Member> listOfMembersWithConcreteCategory = Authentication.getRepository().getMembersByCategory(categoryElement);
                cell.setCellValue(listOfMembersWithConcreteCategory.size());

                //добавление количества строк для всех участников
                for (int i = 1; i < listOfMembersWithConcreteCategory.size() + 1; i++) {
                    for (int j = 0; j < 4; j++) {
                        copyRow(wb, sourceSheet, sheet, 4 + j, (i * 4) + j);
                    }
                }

                //Имена членов жюри в  строку с индексом 3
                row = sheet.getRow(3);

                //Индекс колонки для следующего члена жюри
                int col = 2;
                Map<User, Integer> columnIndexForJuryMarks = new HashMap<>();
                Map<User, Integer> summaryValueForMemberByJury = new HashMap<>();
                for (User juryElement : Authentication.getAllJury()
                ) {
                    summaryValueForMemberByJury.put(juryElement, 0);
                    columnIndexForJuryMarks.put(juryElement, col);
                    cell = row.getCell(col);
                    cell.setCellValue(juryElement.getLastName() + " " + juryElement.getFirstName() + " " + juryElement.getSecondName());
                    col += 4;
                }

                int memberNumberInCategory = 1;
                for (Member memberElement : listOfMembersWithConcreteCategory
                ) {
                    //добавление новой строки для участника


                    //Номер участника
                    row = sheet.getRow(memberNumberInCategory * 4);
                    cell = row.getCell(0);
                    cell.setCellValue(memberNumberInCategory);


                    //Имя участника или коллектива
                    row = sheet.getRow(memberNumberInCategory * 4);
                    cell = row.getCell(1);
                    if (memberElement.getEnsembleName().equals("")) {
                        cell.setCellValue(memberElement.getLastName() + " " + memberElement.getFirstName() + " " + memberElement.getSecondName());
                    } else {
                        cell.setCellValue(memberElement.getEnsembleName());
                    }
                    Map<MARKCRITERIA, Integer> summaryMarkOfFirstSongByCriteria = new HashMap<>();
                    Map<MARKCRITERIA, Integer> summaryMarkOfSecondSongByCriteria = new HashMap<>();
                    Map<MARKCRITERIA, Integer> currentSummaryMarkByCriteria = null;
                    for (MARKCRITERIA criteriaElement : Authentication.getRepository().getAllMarkCriteria()
                    ) {
                        summaryMarkOfFirstSongByCriteria.put(criteriaElement, 0);
                        summaryMarkOfSecondSongByCriteria.put(criteriaElement, 0);
                    }


                    int globalSummary = 0;
                    //Оценки этого участника
                    for (Mark markElement : Authentication.getRepository().getMarksByMember(memberElement)
                    ) {
                        globalSummary += markElement.getValue();

                        if (markElement.getSong().equals(memberElement.getFirstSong())) {
                            row = sheet.getRow((memberNumberInCategory * 4) + 1);
                            currentSummaryMarkByCriteria = summaryMarkOfFirstSongByCriteria;
                        }
                        if (markElement.getSong().equals(memberElement.getSecondSong())) {
                            row = sheet.getRow((memberNumberInCategory * 4) + 2);
                            currentSummaryMarkByCriteria = summaryMarkOfSecondSongByCriteria;
                        }
                        //Итоговые оценки
                        int oldValue = summaryValueForMemberByJury.get(markElement.getJury());
                        summaryValueForMemberByJury.put(markElement.getJury(), oldValue + markElement.getValue());

                        oldValue = currentSummaryMarkByCriteria.get(markElement.getCriteriaOfMark());
                        currentSummaryMarkByCriteria.put(markElement.getCriteriaOfMark(), oldValue + markElement.getValue());


                        //Оценки подробные +
                        cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()));
                        if (markElement.getCriteriaOfMark() == MARKCRITERIA.VOCAL) {
                            cell.setCellValue(markElement.getValue());
                        }

                        if (markElement.getCriteriaOfMark() == MARKCRITERIA.REPERTOIRE) {
                            cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()) + 1);
                            cell.setCellValue(markElement.getValue());
                        }
                        if (markElement.getCriteriaOfMark() == MARKCRITERIA.ARTISTIC) {
                            cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()) + 2);
                            cell.setCellValue(markElement.getValue());
                        }
                        if (markElement.getCriteriaOfMark() == MARKCRITERIA.INDIVIDUALY) {
                            cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()) + 3);
                            cell.setCellValue(markElement.getValue());
                        }

                        //Общая оценка по каждому жюри
                        row = sheet.getRow((memberNumberInCategory * 4) + 3);
                        cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()));
                        cell.setCellValue(summaryValueForMemberByJury.get(markElement.getJury()));
                    }

                    //Заполнение общей оценки за первую песню от всех жюри
                    row = sheet.getRow((memberNumberInCategory * 4) + 1);
                    cell = row.getCell(SUMMARYCOLUMNINDEX);
                    cell.setCellValue(summaryMarkOfFirstSongByCriteria.get(MARKCRITERIA.VOCAL));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 1);
                    cell.setCellValue(summaryMarkOfFirstSongByCriteria.get(MARKCRITERIA.REPERTOIRE));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 2);
                    cell.setCellValue(summaryMarkOfFirstSongByCriteria.get(MARKCRITERIA.ARTISTIC));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 3);
                    cell.setCellValue(summaryMarkOfFirstSongByCriteria.get(MARKCRITERIA.INDIVIDUALY));


                    //Заполнение общей оценки за вторую песню от всех жюри
                    row = sheet.getRow((memberNumberInCategory * 4) + 2);
                    cell = row.getCell(SUMMARYCOLUMNINDEX);
                    cell.setCellValue(summaryMarkOfSecondSongByCriteria.get(MARKCRITERIA.VOCAL));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 1);
                    cell.setCellValue(summaryMarkOfSecondSongByCriteria.get(MARKCRITERIA.REPERTOIRE));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 2);
                    cell.setCellValue(summaryMarkOfSecondSongByCriteria.get(MARKCRITERIA.ARTISTIC));
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 3);
                    cell.setCellValue(summaryMarkOfSecondSongByCriteria.get(MARKCRITERIA.INDIVIDUALY));

                    //Заполнение общей оценки за все песни от всех жюри
                    row = sheet.getRow((memberNumberInCategory * 4) + 3);
                    cell = row.getCell(SUMMARYCOLUMNINDEX);
                    cell.setCellValue(globalSummary);
                    //записываем общую оценку в список для определения места
                    summaryMarksListForPlace.add(globalSummary);


                    //Обнуление общих оценок
                    for (User juryElement : Authentication.getAllJury()
                    ) {
                        summaryValueForMemberByJury.put(juryElement, 0);
                    }

                    memberNumberInCategory++;
                }

                //проставление мест
                //удаляем повторяющиеся значения
                Set<Integer> set = new HashSet<>(summaryMarksListForPlace);
                summaryMarksListForPlace.clear();
                summaryMarksListForPlace.addAll(set);
                Collections.sort(summaryMarksListForPlace);
                Collections.reverse(summaryMarksListForPlace);
                for (int i = 1; i <= listOfMembersWithConcreteCategory.size(); i++) {
                    row = sheet.getRow((i * 4) + 3);
                    cell = row.getCell(SUMMARYCOLUMNINDEX);
                    int summa = (int) cell.getNumericCellValue();
                    row = sheet.getRow(i * 4);
                    cell = row.getCell(SUMMARYCOLUMNINDEX + 4);
                    cell.setCellValue(summaryMarksListForPlace.indexOf(summa) + 1);
                }

                summaryMarksListForPlace.clear();

                //переход к следующей категории
                indexOfSheet++;


            }
            //удаляем лист-рыбу
            wb.removeSheetAt(0);


            FileOutputStream outFile = new FileOutputStream("FullStatement.xls");
            wb.write(outFile);
            outFile.close();
            wb.close();
            fs.close();
            //   Desktop.getDesktop().print(new File("FullStatement.xls"));


            //   ServletContext context = getServletContext();
            ServletOutputStream out = resp.getOutputStream();
            byte[] byteArray = Files.readAllBytes(Paths.get("FullStatement.xls"));
            //данный контент type говорит что будет файл в формате excel
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=FullStatement.xls");
            out.write(byteArray);
            out.flush();
            out.close();


            //          JSONObject jsonObject = new JSONObject();
            //          jsonObject.append("message", "Файлы успешно сохранены.");
            //           resp.setContentType("application/json; charset=UTF-8");
            //          resp.getWriter().write(String.valueOf(jsonObject));
            //          resp.flushBuffer();
            //  req.getRequestDispatcher("/admin/statement/statement.html")
            //        .forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
            resp.sendRedirect("/adminLogin");
        }
    }

}
