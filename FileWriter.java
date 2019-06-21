import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * Most of this file was copied / guided by jballen73's Roster Maker on github
 * https://github.com/jballen73/RosterMaker/blob/master/src/FileWriter.java
 */
public class FileWriter {
    public static void makeRoster(List<Activity> activities) throws Exception {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out =
                new FileOutputStream(new File("Assignment.docx"));
        for (Activity activity : activities) {
            XWPFParagraph title = document.createParagraph();
            title.setPageBreak(true);
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = title.createRun();
            run.setFontSize(28);
            run.setText(activity.getName());
            XWPFRun run2 = title.createRun();
            run2.addBreak();
            run2.setFontSize(22);
            run2.setText(activity.getMeeting_spot());
            XWPFTable table = document.createTable(20, 2);
            table.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "000000");
            //table.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "000000");
            table.setWidth(300);
            List<XWPFTableCell> cellList = new ArrayList<>();
            for (XWPFTableRow row : table.getRows()) {
                cellList.add(row.getCell(0));
                cellList.add(row.getCell(1));
                //row.getCell(1).setText("\t\t\t\t");
                //cellList.add(row.getCell(2));
            }

            int index = 0;
            for (Student student : activity.getStudents()) {
                XWPFTableCell cell = cellList.get(index++);
                cell.removeParagraph(0);
                XWPFRun cellRun =  cell.addParagraph().createRun();
                cellRun.setFontSize(sizeSelector(activity.getStudents().size()));
                cellRun.setText(student.get_name());
            }
        }
        document.write(out);
    }
    public static void makeFile(List<Activity> activities) throws Exception {
        XWPFDocument doc = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File("Activities.docx"));

        for (Activity a : activities) {
            XWPFParagraph title = doc.createParagraph();
            title.setPageBreak(true);
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun activityName = title.createRun();
            activityName.setFontSize(28);
            activityName.setText(a.getName());

            XWPFRun meetingPlace = title.createRun();
            meetingPlace.addBreak();
            meetingPlace.setFontSize(22);
            meetingPlace.setText(a.getMeeting_spot());

            XWPFTable kids = doc.createTable(20, 2);
            kids.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "000000");
            kids.setWidth(3000);
            List<XWPFTableCell> cellList = new ArrayList<>();
            for (XWPFTableRow row : kids.getRows()) {
                cellList.add(row.getCell(0));
                cellList.add(row.getCell(1));
            }
            int i = 0;
            for (Student student : a.getStudents()) {
                XWPFTableCell cell = cellList.get(i++);
                cell.removeParagraph(0);
                XWPFRun cellRun = cell.addParagraph().createRun();
                cellRun.setFontSize(sizeSelector(a.getStudents().size()));
                cellRun.setText(student.get_name());
            }
        }
        doc.write(out);
    }
    private static int sizeSelector(int studentNum) {
        if (studentNum < 20) {
            return 20;
        } else if (studentNum < 25) {
            return 18;
        } else if (studentNum < 30) {
            return 16;
        } else {
            return 14;
        }
    }
}
