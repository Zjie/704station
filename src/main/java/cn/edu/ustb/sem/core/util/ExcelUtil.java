package cn.edu.ustb.sem.core.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
	public static final String DEFAULT_AUTHOR = "ustb zhoujie";
	
	/**
	 * 为一个单元格添加评论
	 * @param cell 该单元格
	 * @param author 作者
	 * @param text 评论内容
	 */
	public static void createCommentForCell(Cell cell, String author, String text) {
		Sheet sheet = cell.getRow().getSheet();
		CreationHelper helper = sheet.getWorkbook().getCreationHelper();
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		cs.setFillForegroundColor(IndexedColors.RED.getIndex());
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(cs);
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor ca = drawing.createAnchor(10, 20, 30, 40, 12, 3, 14, 5);
		Comment com = drawing.createCellComment(ca);
		com.setAuthor(author);
		com.setString(helper.createRichTextString(text));
		cell.setCellComment(com);
	}
	
	public static void createCommentForCell(Cell cell, String text) {
		createCommentForCell(cell, DEFAULT_AUTHOR, text);
	}
	
	public static void addCommentForCell(Cell cell, String text) {
		Comment com = cell.getCellComment();
		if (com == null) {
			//如果没有，重新创建一个
			createCommentForCell(cell, DEFAULT_AUTHOR, "");
			com = cell.getCellComment();
		}
		
		Sheet sheet = cell.getRow().getSheet();
		CreationHelper helper = sheet.getWorkbook().getCreationHelper();
		String exists = com.getString().getString();
		com.setString(helper.createRichTextString(exists + "  " + text));
	}
	
	public static int getRowNum(Sheet sheet) {
		if (sheet == null) {
			return 0;
		}
		int num = 0;
		while (true) {
			Row row = sheet.getRow(num);
			if (row == null) {
				break;
			}
			Cell cell = row.getCell(0);
			if (cell == null || cell.getNumericCellValue() == 0 || cell.getStringCellValue() == null || cell.getStringCellValue().equals("")) {
				break;
			}
			num++;
		}
		return num;
	}
	/**
	 * 通过判断cell的类型进行设置值，以免出错
	 * @param cell
	 */
	public static final Object getCellValue(Cell cell) {
		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_BLANK) {
			return "";
		} else if (type == Cell.CELL_TYPE_BOOLEAN) {
			return cell.getBooleanCellValue();
		}
		
		return null;
	}
}
