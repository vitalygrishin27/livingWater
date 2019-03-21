package adminServlets;

import authentication.Authentication;
import entity.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import repository.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static repository.Utils.copyRow;


@WebServlet("/admin/statement")
public class AdminStatementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Utils.getCurrentTime() + " / START ADMIN STATEMENT SERVLET IS DONE! (GET)");
        if (Authentication.isAdminInDbByCookies(req)) {

            POIFSFileSystem fs = new POIFSFileSystem((new FileInputStream("statement.xls")));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            //Для всех категорий



            for (Category categoryElement : Authentication.getRepository().getAllCategoryFromDB()
            ) {
                //Название категории
                HSSFRow row = sheet.getRow(2);
                HSSFCell cell = row.getCell(2);
                cell.setCellValue(categoryElement.getName());

                //Количество участников в категории
                row = sheet.getRow(2);
                cell = row.getCell(26);

                List<Member> listOfMembersWithConcreteCategory = Authentication.getRepository().getMembersByCategory(categoryElement);
                cell.setCellValue(listOfMembersWithConcreteCategory.size());

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
                    col+=4;
                }

                System.out.println(columnIndexForJuryMarks);
                int memberNumberInCategory = 1;
                for (Member memberElement : listOfMembersWithConcreteCategory
                ) {
                    //Номер участника
                    row = sheet.getRow(4);
                    cell = row.getCell(0);
                    cell.setCellValue(memberNumberInCategory);
                    memberNumberInCategory++;

                    //Имя участника или коллектива
                    row = sheet.getRow(4);
                    cell = row.getCell(1);
                    if (memberElement.getEnsembleName().equals("")) {
                        cell.setCellValue(memberElement.getLastName() + " " + memberElement.getFirstName() + " " + memberElement.getSecondName());
                    } else {
                        cell.setCellValue(memberElement.getEnsembleName());
                    }

                    //Оценки этого участника

                    for (Mark markElement : Authentication.getRepository().getMarksByMember(memberElement)
                    ) {

                        if (markElement.getSong().equals(memberElement.getFirstSong())) row = sheet.getRow(5);
                        if (markElement.getSong().equals(memberElement.getSecondSong())) row = sheet.getRow(6);

                        //Итоговые оценки
                        int oldValue = summaryValueForMemberByJury.get(markElement.getJury());
                        summaryValueForMemberByJury.put(markElement.getJury(), oldValue + markElement.getValue());

                        //Оценки подробные
                        cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()));
                        if (markElement.getCriteriaOfMark() == MARKCRITERIA.VOCAL)
                            cell.setCellValue(markElement.getValue());

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

                        row = sheet.getRow(7);
                        cell = row.getCell(columnIndexForJuryMarks.get(markElement.getJury()));
                        cell.setCellValue(summaryValueForMemberByJury.get(markElement.getJury()));
                        System.out.println(markElement.getValue());
                    }


                }


            }


            //          CellReference cr = new CellReference("AA3");
            //       int r=cr.getRow();
            //         int c=cr.getCol();

/*int r = cr.getRow();
int c = cr.getCol();

Row row = sheet.getRow(r);
if (row == null)
    row = sheet.createRow(r);
Cell cell = row.getCell(c, Row.CREATE_NULL_AS_BLANK);
cell.setCellValue(entry.getValue());*/

/*
            int currentStartRowForCopy=3;
            for (int i = 1; i < Authentication.getRepository().getAllMembersFromDB().size(); i++) {
                for (int j = currentStartRowForCopy; j <currentStartRowForCopy+4 ; j++) {
                    copyRow(wb, sheet, currentStartRowForCopy, currentStartRowForCopy + 4);
                }
               currentStartRowForCopy+=4;
            }*/
   /*
        copyRow(wb,sheet,3,7);
        copyRow(wb,sheet,4,8);
        copyRow(wb,sheet,5,9);
        copyRow(wb,sheet,6,10);


       */

            //     System.out.println(cell.getNumericCellValue());
            //     cell.setCellValue("Кобзев Сергей Сергеевич");
            FileOutputStream outFile = new FileOutputStream("z:\\test.xls");
            wb.write(outFile);

//wb.write();
            outFile.close();
            wb.close();
            fs.close();

            //  System.exit(2);


            req.getRequestDispatcher("/admin/statement/statement.html")
                    .forward(req, resp);
        } else {
            System.out.println(Utils.getCurrentTime() + " / Not authorization. Return to login page.");
            resp.sendRedirect("/adminLogin");
        }
    }

}
