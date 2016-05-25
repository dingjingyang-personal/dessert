package com.dessert.sys.common.tool;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;

    /**
     * 读取Excel数据内容 * @param InputStream * @return Map 包含单元格数据内容的Map对象
     */
    public String[][] readExcel(InputStream is) {

        HSSFRow row;
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            int colNum;
            String[][] content = new String[rowNum + 1][];
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = 0; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int j = 0;
                colNum = row.getLastCellNum();
                content[i] = new String[colNum];
                while (j < colNum) {
                    content[i][j] = getCellFormatValue(row.getCell(j)).trim();
                    j++;
                }
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    /**
     * 根据第一行导出excel为list
     *
     * @param @param  is
     * @param @return 参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     * @Title: readExcelList
     */
    public List<Map<String, Object>> readExcelList(InputStream is) {

        HSSFRow rowTitle;
        HSSFRow row;
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(0);
            int rowNum = sheet.getLastRowNum();
            int colNum;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 1; i <= rowNum; i++) {
                rowTitle = sheet.getRow(0);
                row = sheet.getRow(i);
                int j = 0;
                colNum = row.getLastCellNum();
                Map<String, Object> map = new HashMap<String, Object>();
                while (j < colNum) {
                    map.put(getCellFormatValue(rowTitle.getCell(j)).trim(), getCellFormatValue(row.getCell(j)).trim());
                    j++;
                }
                list.add(map);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    /**
     * 导出excel为list
     *
     * @param @param  is
     * @param @return 参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     * @Title: readExcelList
     */
    public List<Map<String, Object>> readExcelListParams(InputStream is, String[] fields) {
        return readExcelListParams(is, fields, null, null);
    }


    /**
     * 在导出excel为list前，限制长度
     *
     * @param @param  is
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: limitExcelList
     */
    public boolean limitExcelList(InputStream is) {

        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            if (rowNum > 1000) {
                return false;
            } else {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }


    /**
     * 导出excel文件名并根据版本分类处理
     *
     * @param @param  is
     * @param @return 参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     * @Title: judgeExcelList
     */
    public List<Map<String, Object>> judgeExcelList(MultipartFile file, InputStream is, String[] fields, String[] dateField, String[] dateFieldFormat) {
        String name = file.getOriginalFilename();
        int index = file.getOriginalFilename().indexOf(".");
        String suffex = name.substring(index);
        if (".xls".equals(suffex)) {
            return readExcelListParams(is, fields, dateField, dateFieldFormat);
        } else if (".xlsx".equals(suffex)) {
            return readExcelListNew(is, fields, dateField, dateFieldFormat);
        } else {
            return null;
        }

    }

    /**
     * 导出excel为list（excel2007-2010版）
     *
     * @param @param  is
     * @param @return 参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     * @Title: readExcelListNew
     */
    public List<Map<String, Object>> readExcelListNew(InputStream is, String[] fields, String[] dateField, String[] dateFieldFormat) {

        XSSFRow row;
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(is);
            XSSFSheet sheetnew = xwb.getSheetAt(0);
            row = sheetnew.getRow(0);
            int rowNum = sheetnew.getLastRowNum();
            int colNum;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> mapid = new HashMap<String, Object>();
            int m = 0;
            for (String temp : dateField) {
                mapid.put(temp, dateFieldFormat[m++]);
            }

            for (int i = 1; i <= rowNum; i++) {
                row = sheetnew.getRow(i);
                int j = 0;
                colNum = row.getLastCellNum();
                Map<String, Object> map = new HashMap<String, Object>();
                while (j < colNum && j < fields.length) {
                    if (mapid.containsKey(fields[j])) {
                        map.put(fields[j], getCellFormatValueNew(row.getCell(j), String.valueOf(mapid.get(fields[j]))).trim());
                    } else {
                        map.put(fields[j], getCellFormatValueNew(row.getCell(j)).trim());
                    }
                    j++;
                }
                list.add(map);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    /**
     * 导出excel为list（excel2003版）
     *
     * @param @param  is
     * @param @return 参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     * @Title: readExcelList
     */
    public List<Map<String, Object>> readExcelListParams(InputStream is, String[] fields, String[] dateField, String[] dateFieldFormat) {

        HSSFRow row;
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(0);
            int rowNum = sheet.getLastRowNum();
            int colNum;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> mapid = new HashMap<String, Object>();
            int m = 0;
            for (String temp : dateField) {
                mapid.put(temp, dateFieldFormat[m++]);
            }

            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int j = 0;
                colNum = row.getLastCellNum();
                Map<String, Object> map = new HashMap<String, Object>();
                while (j < colNum && j < fields.length) {
                    if (mapid.containsKey(fields[j])) {
                        map.put(fields[j], getCellFormatValue(row.getCell(j), String.valueOf(mapid.get(fields[j]))).trim());
                    } else {
                        map.put(fields[j], getCellFormatValue(row.getCell(j)).trim());
                    }
                    j++;
                }
                list.add(map);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    /**
     * * 获取单元格数据内容为字符串类型的数据 * * @param cell Excel单元格 * @return String 单元格数据内容
     * */
    // private String getStringCellValue(HSSFCell cell) {
    // if (cell == null) {
    // return "";
    // }
    // String strCell = "";
    // switch (cell.getCellType()) {
    // case HSSFCell.CELL_TYPE_STRING:
    // strCell = cell.getStringCellValue();
    // break;
    // case HSSFCell.CELL_TYPE_NUMERIC:
    // strCell = String.valueOf(cell.getNumericCellValue());
    // break;
    // case HSSFCell.CELL_TYPE_BOOLEAN:
    // strCell = String.valueOf(cell.getBooleanCellValue());
    // break;
    // case HSSFCell.CELL_TYPE_BLANK:
    // strCell = "";
    // default:
    // strCell = cell.getStringCellValue();
    // break;
    // }
    // if (strCell.equals("") || strCell == null) {
    // return "";
    // }
    // return strCell;
    // }

    /**
     * * 获取单元格数据内容为日期类型的数据 * * @param cell * Excel单元格 * @return String 单元格数据内容
     */
    // private String getDateCellValue(HSSFCell cell) {
    // String result = "";
    // try {
    // int cellType = cell.getCellType();
    // if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
    // Date date = cell.getDateCellValue();
    // result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
    // + "-" + date.getDate();
    // } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
    // String date = getStringCellValue(cell);
    // result = date.replaceAll("[年月]", "-").replace("日", "").trim();
    // } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
    // result = "";
    // }
    // } catch (Exception e) {
    // System.out.println("日期格式不正确!");
    // e.printStackTrace();
    // }
    // return result;
    // }
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 根据HSSFCell类型设置数据 * @param cell * @return
     */


    private String getCellFormatValue(HSSFCell cell) {
        return getCellFormatValue(cell, null);
    }

    private String getCellFormatValueNew(XSSFCell cell) {
        return getCellFormatValueNew(cell, null);
    }

    private String getCellFormatValueNew(XSSFCell cell, String dateFormat) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (dateFormat == null || dateFormat == "") {
                            cellvalue = sdf.format(date);
                        } else {
                            SimpleDateFormat sdFormat = new SimpleDateFormat(dateFormat);
                            cellvalue = sdFormat.format(date);
                        }
                    } // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = new BigDecimal(Double.toString(cell.getNumericCellValue())).toPlainString();
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    private String getCellFormatValue(HSSFCell cell, String dateFormat) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (dateFormat == null || dateFormat == "") {
                            cellvalue = sdf.format(date);
                        } else {
                            SimpleDateFormat sdFormat = new SimpleDateFormat(dateFormat);
                            cellvalue = sdFormat.format(date);
                        }
                    } // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = new BigDecimal(Double.toString(cell.getNumericCellValue())).toPlainString();
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * 功能描述: 输出excel表格
     * author:liwm
     *
     * @param title
     * @param fields
     * @param data
     * @param response
     */
    public void writeExcel(String sheetName, String[] title, String[] fields, List<Map<String, Object>> data,
                           OutputStream outputStream) {
        if (fields == null || fields.length == 0) {
            return;
        }
        if (data == null || data.size() == 0) {
            return;
        }
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(StringUtils.isEmpty(sheetName) ? "报表" : sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row;
        int index = 0;
        HSSFCell cell;
        if (title != null && title.length != 0) {
            row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            for (int i = 0; i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
            }
            index = 1;
        }

        int size = data.size();
        int[] widths = new int[title.length];
        String dataString;
        for (int i = 0; i < size; i++) {
            row = sheet.createRow(i + index);
            for (int j = 0; j < fields.length; j++) {
                dataString = SysToolHelper.getMapValue(data.get(i), fields[j]);
                widths[j] = Math.max(widths[j], dataString.getBytes().length);
                row.createCell(j).setCellValue(dataString);
            }
        }
        for (int i = 0; i < title.length; i++) {
            sheet.setColumnWidth(i, Math.max(widths[i], 20) * 256);
            //sheet.autoSizeColumn(i);
        }
        try {
            wb.write(outputStream);
        } catch (IOException e) {
            SysToolHelper.error("outputExcel", e);
        }

    }

    /**
     * 多个工作表导出
     *
     * @param sheetName
     * @param titleList
     * @param fieldsList
     * @param data
     * @param outputStream void
     * @throws
     * @Title: writeExcelMoreSheet
     */
    public void writeExcelMoreSheet(List<String> sheetNameList, List<String[]> titleList, List<String[]> fieldsList,
                                    List<List<Map<String, Object>>> data, OutputStream outputStream) {
        if (fieldsList == null || fieldsList.size() == 0) {
            return;
        }
        if (data == null || data.size() == 0) {
            return;
        }
        if (!(sheetNameList.size() == titleList.size() && titleList.size() == fieldsList.size() && fieldsList.size() == data.size())) {
            throw new RuntimeException();
        }
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        for (int z = 0; z < sheetNameList.size(); z++) {

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet(StringUtils.isEmpty(sheetNameList.get(z)) ? "报表" : sheetNameList.get(z));
            wb.setSheetName(z, sheetNameList.get(z));
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row;
            int index = 0;
            HSSFCell cell;
            if (titleList.get(z) != null && titleList.get(z).length != 0) {
                row = sheet.createRow(0);
                // 第四步，创建单元格，并设置值表头 设置表头居中
                HSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
                for (int i = 0; i < titleList.get(z).length; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(titleList.get(z)[i]);
                }
                index = 1;
            }

            int size = data.get(z).size();
            for (int i = 0; i < size; i++) {
                row = sheet.createRow(i + index);
                for (int j = 0; j < fieldsList.get(z).length; j++) {
                    row.createCell(j).setCellValue(SysToolHelper.getMapValue(data.get(z).get(i), fieldsList.get(z)[j]));
                }
            }
        }
        try {
            wb.write(outputStream);
        } catch (IOException e) {
            SysToolHelper.error("outputExcel", e);
        }

    }


}
