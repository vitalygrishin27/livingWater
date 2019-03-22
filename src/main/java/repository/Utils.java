package repository;

import authentication.Authentication;
import entity.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    public static JSONObject getJsonFromRequest(HttpServletRequest req) {
        JSONObject result;

        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);


            result = new JSONObject(jb.toString());
            System.out.println(result);

            return result;
        } catch (Exception e) {
            System.out.println("Error with buffered reader in getJsonFromRequest method.");
        }

        return new JSONObject();
    }

    public static Member getSoloMemberFromJson(JSONObject jsonObject) {
        return BuilderMember.getBuilderMember().setId(Authentication.getRepository().getFreeIdOfMembersDB())
                .setFirstName(jsonObject.getString("firstname"))
                .setSecondName(jsonObject.getString("secondname"))
                .setLastName(jsonObject.getString("lastname"))
                .setBirth(getDateFromString(jsonObject.getString("birth")))
                .setEnsembleName("")
                .setCountOfMembers(1)
                .setGender(Gender.getGenderByChar(jsonObject.getString("gender")))
                .setOffice(jsonObject.getString("office"))
                .setAddress(getAddressForSoloMemberFromJson(jsonObject))
                .setPassport(jsonObject.getString("passport"))
                .setINN(jsonObject.getString("INN"))
                .setBoss(jsonObject.getString("boss"))
                .setCategory(Authentication.getRepository().getCategoryByName(jsonObject.getString("category")))
                .setFirstSong(createFirstSongByName(jsonObject.getString("firstSong")))
                .setSecondSong(createSecondSongByName(jsonObject.getString("secondSong")))
                .setRegistration(false)
                .setTurnNumber(Authentication.getRepository().getFreeTurnNumberFromMemberDB())
                .build();

    }

    public static Member getEnsembleFromJson(JSONObject jsonObject) {
        return BuilderMember.getBuilderMember().setId(Authentication.getRepository().getFreeIdOfMembersDB())
                .setFirstName("")
                .setSecondName("")
                .setLastName("")
                .setBirth(getDateFromString("1985-03-27"))
                .setEnsembleName(jsonObject.getString("ensembleName"))
                .setCountOfMembers(jsonObject.getInt("countOfMembers"))
                .setGender(Gender.getGenderByChar("M"))
                .setOffice(jsonObject.getString("ensembleOffice"))
                .setAddress(getAddressForSoloMemberFromJson(jsonObject))
                .setPassport("")
                .setINN("")
                .setBoss(jsonObject.getString("boss"))
                .setCategory(Authentication.getRepository().getCategoryByName(jsonObject.getString("category")))
                .setFirstSong(createFirstSongByName(jsonObject.getString("firstSong")))
                .setSecondSong(createSecondSongByName(jsonObject.getString("secondSong")))
                .setRegistration(false)
                .setTurnNumber(Authentication.getRepository().getFreeTurnNumberFromMemberDB())
                .build();

    }



    public static User getJuryFromJson(JSONObject jsonObject) {

        return BuilderUserJury.getBuilderUserJury().setFirstName(jsonObject.getString("firstName"))
                .setSecondName(jsonObject.getString("secondName"))
                .setLastName(jsonObject.getString("lastName"))
                .setUserName(jsonObject.getString("userName"))
                .setPassword(jsonObject.getString("password"))
                .setOffice(jsonObject.getString("office"))
                .setRole(Authentication.getRepository().createRoleByName("JURY"))
                .build();
    }

    private static Song createFirstSongByName(String name) {
        Song song = new Song();
        song.setId(Authentication.getRepository().getFreeIdOfSongDB());
        song.setName(name);
        return song;
    }

    private static Song createSecondSongByName(String name) {
        Song song = new Song();
        song.setId((Authentication.getRepository().getFreeIdOfSongDB()) + 1);
        song.setName(name);
        return song;
    }


    private static Address getAddressForSoloMemberFromJson(JSONObject jsonObject) {
        return BuilderAddress.getBuilderAddress().setId(Authentication.getRepository().getFreeIdOfAddressDB())
                .setCountry(jsonObject.getString("country"))
                .setRegion(jsonObject.getString("region"))
                .setDistrict(jsonObject.getString("district"))
                .setCity(jsonObject.getString("city"))
                .setPhone(jsonObject.getString("phone"))
                .build();


    }



    private static Date getDateFromString(String d) {
        Date docDate = null;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        try {
            docDate = format.parse(d);
        } catch (ParseException e) {
            System.out.println("Ошибка в приведении даты из строки (" + d + ")");
            e.printStackTrace();
        }
        return docDate;
    }


    public static String getCurrentTime() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.HOUR) + ":" +
                gcalendar.get(Calendar.MINUTE) + ":" +
                gcalendar.get(Calendar.SECOND);

    }


    public static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());
      //    System.out.println(oldCell.getCellType());
          //  System.exit(2);

            // Set the cell data value
           switch (oldCell.getCellType()) {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
            //    case ERROR:
          //          newCell.setCellErrorValue(oldCell.getErrorCellValue());
         //           break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }
}


