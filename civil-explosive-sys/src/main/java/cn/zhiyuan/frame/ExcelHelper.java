package cn.zhiyuan.frame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 数据读取工具类，POI实现，兼容Excel2003，及Excel2007
 * 注意: 不管是 工作表 列 以及 行 都是从0开始检索
 * <p/>
 * 警告: readExcel 与readCell 不能在 同一个方法内使用 因为 他们如果使用同一 输入流
 * 他们 都是在 执行万自己的内 关闭输入流 造成下一个方法的错误
 * 除非他们使用的不是统一个 输入流
 * <p/>
 * Created by Administrator on 2015/5/5.
 */
public class ExcelHelper {

	protected static Log logger = LogFactory.getLog(BaseAction.class);

    /**
     * 索引开始值
     */
    public static final int INIT_INDEX = 0;
    
    /**
     * 第三推荐使用
     * 所有参入都需要配置 如果你不介意为所有参数赋值 可以尽情使用 可以给你带来更为灵活的读出操作
     *
     * @param inputStream   输入流
     * @param sheetIndex    工作表的索引值(从0检索 )
     * @param maxRowIndex   最多列的所在行 索引值 (从0检索) 即没有空值的行 或者最后一列不为空的行
     * @param dataRoleIndex 数据行 索引值(从0检索)
     * @return List
     */
    public static List<List<String>> readExcel(InputStream inputStream, 
    		int sheetIndex, int maxRowIndex, int dataRoleIndex) {
        List<List<String>> resultList = null;
        //设置 工作表 索引默认值 0
        if (sheetIndex < INIT_INDEX)
            sheetIndex = INIT_INDEX;
        //设置工作表列的索引默认值0
        if (maxRowIndex < INIT_INDEX)
            maxRowIndex = INIT_INDEX;

        if (dataRoleIndex < INIT_INDEX)
            dataRoleIndex = INIT_INDEX;
        try {
            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(sheetIndex);
            //1.获取总行数
            int rowNum = sheet.getLastRowNum();
            //2.2 获取总列数 //采用获取最大数据列的所在的行的总列数 不采用2.1 方式
            int cellNum = sheet.getRow(maxRowIndex).getLastCellNum();
            if (cellNum < INIT_INDEX || rowNum<INIT_INDEX)
                return null;
            //从数据行 开始检索
            resultList = new ArrayList<>();
            for (int i = dataRoleIndex; i <= rowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                //2.1 获取总列数 int cellNum = row.getLastCellNum(); //不在采用次方读出列数 因为如果改行有空置 则指挥取到 有值的列数
                List<String> list = new ArrayList<>();
                for (int j = INIT_INDEX; j < cellNum; j++) {
                    Cell cell = row.getCell(j);
                    //为空就 置为null值
                    if (cell == null) {
                        list.add(null);
                        continue;
                    }
                    list.add(getCellValue(cell).trim());
                }
                resultList.add(list);
            }
        } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("没有数据流");
            }
        }
        return resultList;
    }


    /**
     * 动态方式读出Excel 表格 每列的单元格的数量 不包含 为空的单位格 适合tree结构
     * @param inputStream
     * @param sheetIndex
     * @param dataRoleIndex
     * @return
     */
    public List<List<String>> readDyExcel(InputStream inputStream, int sheetIndex, int dataRoleIndex) {
        List<List<String>> resultList = null;
        //设置 工作表 索引默认值 0
        if (sheetIndex < INIT_INDEX)
            sheetIndex = INIT_INDEX;

        if (dataRoleIndex < INIT_INDEX)
            dataRoleIndex = INIT_INDEX;
        try {
            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(sheetIndex);
            //1.获取总行数
            int rowNum = sheet.getLastRowNum();
            //从数据行 开始检索
            resultList = new ArrayList<>();
            for (int i = dataRoleIndex; i <= rowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                //2.1 获取总列数  //继续采用次方读出列数 为null 的 单元格 不再读出
                int cellNum = row.getLastCellNum();
                List<String> list = new ArrayList<>();
                for (int j = INIT_INDEX; j < cellNum; j++) {
                    Cell cell = row.getCell(j);
                    //为空就 置为null值
                    if (cell == null) {
                        list.add(null);
                        continue;
                    }
                    list.add(getCellValue(cell));
                }
                resultList.add(list);
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("没有数据流");
            }
        }
        return resultList;
    }


    private static String getCellValue(Cell cell) {
        //int cellType = cell.getCellType();
        CellType cellType = cell.getCellTypeEnum();
        String result = "";
        if(CellType.NUMERIC == cellType) {//数字
        	if (DateUtil.isCellDateFormatted(cell)) {
        		result = CommonUtils.date2str(cell.getDateCellValue(),"yyyy-MM-dd");
            } else {
                DecimalFormat df = new DecimalFormat("#");
                result =  df.format(cell.getNumericCellValue());
            }
        }else if(CellType.BLANK == cellType) {//空行
        	result =  "";
        }else if(CellType.BOOLEAN == cellType) {
        	result =  String.valueOf(cell.getBooleanCellValue());
        }else if(CellType.STRING == cellType) {
        	result = cell.getStringCellValue();
        }else if(CellType.ERROR == cellType) {
        	result = "";
        }else if(CellType.FORMULA == cellType) {
        	String cellValue = cell.getStringCellValue();
        	result = cellValue == null?"":cellValue.replace("#N/A", "").trim();
        }
        return result;
    }

    /**
     * 不建议使用的
     * 读出方式 除非列标题行就是开始行 而不是工作表的标题行 或者你很熟悉该方法的
     * 否则获取的列数是不固定的 如果有空单元格 会造成list 索引不固定
     *
     * @param inputStream 输入流
     * @return List
     */
    public List<List<String>> readExcel(InputStream inputStream) {
        return readExcel(inputStream, INIT_INDEX, INIT_INDEX);
    }


    /**
     * 第二推荐的 读出方式
     * 通用的 excel 读出方法将 默认读出第一个工作表的数据
     * 根据参入的 标题行计算列数,从默认行(开始行)开始读出数据
     * 所有的 索引从0开始
     *
     * @param inputStream   输入流
     * @param maxRowIndex 最多列的所在行 索引值 (从0检索) 即没有空值的行 或者最后一列不为空的行
     * @return
     */
    public List<List<String>> readExcel(InputStream inputStream, int maxRowIndex) {
        return readExcel(inputStream, maxRowIndex, INIT_INDEX);
    }
    /**
     * 第二推荐的 读出方式
     * 通用的 excel 读出方法将 默认读出第一个工作表的数据
     * 根据参入的 标题行计算列数,从默认行(开始行)开始读出数据
     * 所有的 索引从0开始
     *
     * @param inputStream   输入流
     * @return
     */
    public List<List<String>> readDyExcel(InputStream inputStream) {
        return readDyExcel(inputStream, INIT_INDEX);
    }

    /**
     * 推荐的 读出 excel 方式
     * 默认读出第一个工作表的数据
     * 根据参入的 标题行计算列数,从参入的数据行开始读出数据
     * 所有的 索引从0开始
     *
     * @param inputStream   输入流
     * @param maxRowIndex 最多列的所在行 索引值 (从0检索) 即没有空值的行 或者最后一列不为空的行 <优先>选择标题行
     * @param dataRowIndex  数据行的索引值 (行数 减去1)
     * @return List
     */
    public List<List<String>> readExcel(InputStream inputStream, int maxRowIndex, int dataRowIndex) {
        return readExcel(inputStream, INIT_INDEX, maxRowIndex, dataRowIndex);
    }


    /**
     * 推荐的 动态读出 excel 方式
     * 默认读出第一个工作表的数据
     * 根据参入的 标题行计算列数,从参入的数据行开始读出数据
     * 所有的 索引从0开始
     *
     * @param inputStream   输入流
     * @param dataRowIndex  数据行的索引值 (行数 减去1)
     * @return List
     */
    public List<List<String>> readDyExcel(InputStream inputStream, int dataRowIndex) {
        return readDyExcel(inputStream, INIT_INDEX, dataRowIndex);
    }
    /**
     * 通用的 获取上传的 excel 文件的 某一个工作表的某一行的 某一列值
     *
     * @param inputStream excel 文件流
     * @param sheetIndex  工作表索引(默认为0)
     * @param rowIndex    行索引(默认值0)
     * @param cellIndex   列索引默认值 0
     * @return
     */
    public String readCell(InputStream inputStream, int sheetIndex, int rowIndex, int cellIndex) {
        String cellVaule = null;
        //设置 工作表 起始索引默认值 0
        if (sheetIndex < INIT_INDEX)
            sheetIndex = INIT_INDEX;
        //设置工作表列的起始索引默认值 0
        if (rowIndex < INIT_INDEX)
            rowIndex = INIT_INDEX;
        //列 起始索引值 0
        if (cellIndex < INIT_INDEX) {
            cellIndex = INIT_INDEX;
        }
        try {
            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int rowNum = sheet.getLastRowNum();
            if (rowNum > rowIndex) {
                Row row = sheet.getRow(rowIndex);
                int cellNum = row.getLastCellNum();
                if (cellNum > cellIndex) {
                    Cell cell = row.getCell(cellIndex);
                    cellVaule = getCellValue(cell);
                }
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("没有数据流");
            }
        }

        return cellVaule;

    }

    public String readCell(InputStream inputStream) {
        return readCell(inputStream, INIT_INDEX, INIT_INDEX, INIT_INDEX);
    }

    public String readCell(InputStream inputStream, int rowIndex, int cellIndex) {
        return readCell(inputStream, INIT_INDEX, rowIndex, cellIndex);
    }

    public String readCell(InputStream inputStream, int cellIndex) {
        return readCell(inputStream, INIT_INDEX, INIT_INDEX, cellIndex);
    }

    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream("/home/wen/文档/民爆/爆破委托单位明细(1).xlsx");
            ExcelHelper excelHelper = new ExcelHelper();
            List<List<String>> data = excelHelper.readDyExcel(inputStream, 2);
            StringBuilder sb = new StringBuilder();
            int size = 0;
            for (List<String> list : data) {
            	if(list.size() < 2) continue;
            	String formatStr = "insert into delegation_unit(name,contacts)"
            			+ " values('%s','%s');\n";
            	String sql = String.format(formatStr, list.get(1),list.get(2).trim());
            	sb.append(sql);
            	//if(size++ > 10) break;
            }
            System.out.println(sb);
            CommonUtils.writeFile("/home/wen/下载/bb.sql", sb.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
