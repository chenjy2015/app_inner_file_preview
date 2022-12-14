package com.filepreview.application.office;

import android.util.Log;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * excel to html
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/28
 */
public class ExcelToHtml {

    public static String readExcelToHtml(String xlsPath) {
        try {
            Workbook wb = readExcel(xlsPath);
            return excel07ToHtml(wb);
//            if (wb instanceof XSSFWorkbook) {
//                XSSFWorkbook XWb = (XSSFWorkbook) wb;
//                return excel07ToHtml(XWb);
//            } else if (wb instanceof HSSFWorkbook) {
//                HSSFWorkbook HWb = (HSSFWorkbook) wb;
//                return excel03ToHtml(HWb);
//            } else {
//                return new String("Temporary does not support.".getBytes(), StandardCharsets.UTF_8);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ExcelToHtml", "e:" + e.getMessage());
        }
        return null;
    }

    private static Workbook readExcel(String fileName) {
        Workbook wb = null;
        if (fileName == null) {
            return null;
        }
        String extString = fileName.substring(fileName.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * excel03???html
     * filename:?????????????????????????????????
     * filepath:?????????
     * htmlname:??????html??????
     * path:html????????????
     */
    public static String excel03ToHtml(HSSFWorkbook excelBook) throws ParserConfigurationException, TransformerException, IOException {
        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        excelToHtmlConverter.processWorkbook(excelBook);//excel???html
        Document htmlDocument = excelToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();//?????????????????????
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        /** ???document????????????????????????????????????html?????? */
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        return outStream.toString("UTF-8");
    }

    private static Map<String, Object> map[];


    /**
     * excel07???html
     * filename:?????????????????????????????????
     * filepath:?????????
     * htmlname:??????html??????
     * path:html????????????
     */
    public static String excel07ToHtml(Workbook workbook) {
        ByteArrayOutputStream baos = null;
        StringBuilder html = new StringBuilder();
        try {
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                html.append("=======================").append(sheet.getSheetName()).append("=========================<br><br>");

                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();
//                html.append("<table style='border-collapse:collapse;width:100%;' align='left'>");
                html.append("<table style='" +
                        "        font-size:11px;" +
                        "        color:#333333;" +
                        "        border-width: 0.1px;" +
                        "        border-color: #666666;" +
                        "        border-collapse: collapse;width:100%;' align='left'>");

                map = getRowSpanColSpanMap(sheet);
                //???
                for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);
                    if (null == currentRow) {
                        html.append("<tr><td >  </td></tr>");
                        continue;
                    } else if (currentRow.getZeroHeight()) {
                        continue;
                    }
                    html.append("<tr>");
                    int firstColumnIndex = currentRow.getFirstCellNum();
                    int lastColumnIndex = currentRow.getLastCellNum();
                    //???
                    for (int columnIndex = firstColumnIndex; columnIndex <= lastColumnIndex; columnIndex++) {
                        Cell currentCell = currentRow.getCell(columnIndex);
                        if (currentCell == null) {
                            continue;
                        }
                        String currentCellValue = getCellValue(currentCell);
                        if (map[0].containsKey(rowIndex + "," + columnIndex)) {
                            String pointString = (String) map[0].get(rowIndex + "," + columnIndex);
                            int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                            int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                            int rowSpan = bottomeRow - rowIndex + 1;
                            int colSpan = bottomeCol - columnIndex + 1;
                            if (map[2].containsKey(rowIndex + "," + columnIndex)) {
                                rowSpan = rowSpan - (Integer) map[2].get(rowIndex + "," + columnIndex);
                            }
                            html.append("<td style='border-width: 0.1px;" +
                                    "        border-style: solid;" +
                                    "        border-color: #666666;" +
                                    "        background-color: #ffffff;'")
                                    .append("rowspan= '")
                                    .append(rowSpan)
                                    .append("' colspan= '")
                                    .append(colSpan)
                                    .append("' ");
                            if (map.length > 3 && map[3].containsKey(rowIndex + "," + columnIndex)) {
                                //??????????????????????????????value???????????????????????????????????????
                                currentCellValue = getMergedRegionValue(sheet, rowIndex, columnIndex);
                            }
                        } else if (map[1].containsKey(rowIndex + "," + columnIndex)) {
                            map[1].remove(rowIndex + "," + columnIndex);
                            continue;
                        } else {
                            html.append("<td style='border-width: 0.1px;" +
                                    "        border-style: solid;" +
                                    "        border-color: #666666;" +
                                    "        background-color: #ffffff;' ");
                        }
                        CellStyle cellStyle = currentCell.getCellStyle();
                        if (cellStyle != null) {
                            html.append("align='").append(getHAlignByExcel(cellStyle.getAlignmentEnum())).append("' ");//????????????????????????????????????
                            html.append("valign='").append(getVAlignByExcel(cellStyle.getVerticalAlignmentEnum())).append("' ");//???????????????????????????????????????
                        }
                        html.append(">");
                        if (currentCellValue != null && !"".equals(currentCellValue)) {
                            html.append(currentCellValue.replace(String.valueOf((char) 160), " "));
                        }
                        html.append("</td>");
                    }
                    html.append("</tr>");
                }
                html.append("</table>");

                baos = new ByteArrayOutputStream();
                DOMSource domSource = new DOMSource();
                StreamResult streamResult = new StreamResult(baos);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
                baos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String(html.toString().getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * ??????excel????????????????????????????????????????????????????????????html???????????????????????????
     *
     * @param sheet
     * @return
     */
    private static Map<String, Object>[] getRowSpanColSpanMap(Sheet sheet) {
        Map<String, String> map0 = new HashMap<String, String>();    //??????????????????????????????????????????????????????
        Map<String, String> map1 = new HashMap<String, String>();    //?????????????????????????????????
        Map<String, Integer> map2 = new HashMap<String, Integer>();    //?????????????????????????????????
        Map<String, String> map3 = new HashMap<String, String>();    //??????????????????????????????????????????????????????????????????
        int mergedNum = sheet.getNumMergedRegions();
        CellRangeAddress range = null;
        Row row = null;
        for (int i = 0; i < mergedNum; i++) {
            range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            /**
             * ??????????????????????????????????????????
             * 1.?????????????????????????????????????????????poi???????????????
             */
            if (topRow != bottomRow) {
                int zeroRoleNum = 0;
                int tempRow = topRow;
                for (int j = topRow; j <= bottomRow; j++) {
                    row = sheet.getRow(j);
                    if (row.getZeroHeight() || row.getHeight() == 0) {
                        if (j == tempRow) {
                            //???????????????????????????rowTop?????????
                            tempRow++;
                            continue;//??????top?????????????????????rowSpan?????????????????????????????????????????????zeroRoleNum;
                        }
                        zeroRoleNum++;
                    }
                }
                if (tempRow != topRow) {
                    map3.put(tempRow + "," + topCol, topRow + "," + topCol);
                    topRow = tempRow;
                }
                if (zeroRoleNum != 0) map2.put(topRow + "," + topCol, zeroRoleNum);
            }
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, topRow + "," + topCol);
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = {map0, map1, map2, map3};
        System.err.println(map0);
        return map;
    }

    /**
     * ???????????????????????????
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);

                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }

    /**
     * ???????????????
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }


    private static String getVAlignByExcel(VerticalAlignment align) {
        String result = "middle";
        if (align == VerticalAlignment.BOTTOM) {
            result = "bottom";
        }
        if (align == VerticalAlignment.CENTER) {
            result = "center";
        }
        if (align == VerticalAlignment.JUSTIFY) {
            result = "justify";
        }
        if (align == VerticalAlignment.TOP) {
            result = "top";
        }
        return result;
    }

    protected static String getHAlignByExcel(HorizontalAlignment align) {
        String result = "left";
        if (align == HorizontalAlignment.LEFT) {
            result = "left";
        }
        if (align == HorizontalAlignment.RIGHT) {
            result = "right";
        }
        if (align == HorizontalAlignment.JUSTIFY) {
            result = "justify";
        }
        if (align == HorizontalAlignment.CENTER) {
            result = "center";
        }
        return result;
    }

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }

}