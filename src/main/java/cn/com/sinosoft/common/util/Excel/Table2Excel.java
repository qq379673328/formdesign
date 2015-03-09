/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-12-19
 */
package cn.com.sinosoft.common.util.Excel;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-12-19
 */
public class Table2Excel {

	private static final int ExcelTransTd = 0;
	private Workbook wb = null;
	private Sheet sheet = null;
	private Row row = null;
	private int rowIdx = 0;
	private Cell cell = null;
	private int cellIdx = 0;
	private List<Integer[]> mergeCells = new ArrayList<Integer[]>();
	private Map<String, Object> mergeFlag = new HashMap<String, Object>();
	private CellStyle cellStyle;
	private int colMax = 0;
	
	public Table2Excel(){
		wb = new HSSFWorkbook();
		cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom((short)1);
        cellStyle.setBorderTop((short)1);
        cellStyle.setBorderLeft((short)1);
        cellStyle.setBorderRight((short)1);
        cellStyle.setBottomBorderColor(HSSFFont.COLOR_NORMAL);
        cellStyle.setTopBorderColor(HSSFFont.COLOR_NORMAL);
        cellStyle.setLeftBorderColor(HSSFFont.COLOR_NORMAL);
        cellStyle.setRightBorderColor(HSSFFont.COLOR_NORMAL);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
	}
	
	/**
	 * 将xml格式table，转换为excel输出流<br/>
	 * 由于jquery在ie下使用.html()获取的内容不符合xml规范，无法解析通过，故此方法废弃，但正常的xml可以解析，格式如:<br/>
	 * &lt;table>&lt;tr>&lt;td>&lt;/td>&lt;/tr>&lt;/table>;
	 *
	 * @param tableJson
	 * @param outputStream
	 * @throws Exception
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Deprecated
	public void transXml2Excel(String tableJson, OutputStream outputStream) throws Exception{
		tableJson = handleTableJson(tableJson);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		Document document = db.parse(new ByteArrayInputStream(tableJson.getBytes("UTF-8"))); 
		NodeList rootNodeList = document.getChildNodes();
		Node node = rootNodeList.item(0);
		//处理节点
		handleNode(node, wb);
		//合并单元格
		mergeCell();
		//调整列宽
		autoCell();
		wb.write(outputStream);
	}
	
	/**
	 * 处理节点
	 *
	 * 
	 * @param node
	 * @param wb
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private void handleNode(Node node, Workbook wb){
		String nodeName = node.getNodeName();
		//当前节点处理
		NamedNodeMap nodeAttrs = node.getAttributes();
		if(nodeName.equals("table") || nodeName.equals("TABLE")){
			sheet = wb.createSheet();
		}else if(nodeName.equals("thead") || nodeName.equals("THEAD")){
		}else if(nodeName.equals("tbody") || nodeName.equals("TBODY")){
		}else if(nodeName.equals("tfoot") || nodeName.equals("TFOOT")){
		}else if(nodeName.equals("tr") || nodeName.equals("TR")){
			row = sheet.createRow(rowIdx);
			rowIdx++;
			cellIdx = 0;
		}else if(nodeName.equals("td") || nodeName.equals("th")
				 || nodeName.equals("TD") || nodeName.equals("TH")){
			while(mergeFlag.get("" + (rowIdx -1) + "," + cellIdx) != null){
				cellIdx++;
			}
			cell = row.createCell(cellIdx);
			cell.setCellValue(node.getTextContent());
			cell.setCellStyle(cellStyle);
			Node colSpanNode = nodeAttrs.getNamedItem("colspan") == null ? 
					nodeAttrs.getNamedItem("COLSPAN") : nodeAttrs.getNamedItem("colspan");
			Node rowSpanNode = nodeAttrs.getNamedItem("rowspan") == null ?
					nodeAttrs.getNamedItem("ROWSPAN") : nodeAttrs.getNamedItem("rowspan");;
			int cols = 1;
			int rows = 1;
			if(colSpanNode != null){
				String c = colSpanNode.getNodeValue();
				cols = c == null ? 1 : Integer.valueOf(c);
			}
			if(rowSpanNode != null){
				String r = rowSpanNode.getNodeValue();
				rows = r == null ? 1 : Integer.valueOf(r);
			}
			//处理合并表头
			int beginRow = rowIdx -1;
			int endRow = rowIdx -1 + rows -1;
			int beginCol = cellIdx;
			int endCol = cellIdx + cols - 1;
			if(endRow != beginRow || endCol != beginCol){
				mergeCells.add(new Integer[]{beginRow, endRow, beginCol, endCol});
			}
			for(int i = beginRow; i<= endRow; i++){
				for(int j = beginCol; j <= endCol; j++){
					mergeFlag.put("" + i + "," + j, true);
				}
			}
			
			cellIdx = cellIdx + cols;
			colMax = cellIdx > colMax ? cellIdx : colMax;
		}
		//子节点
		NodeList nodeList = node.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++){
			Node oneNode = nodeList.item(i);
			handleNode(oneNode, wb);
		}
	}
	
	/**
	 * 处理tablejson
	 *
	 * 
	 * @param tableJson
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private String handleTableJson(String tableJson){
		if(tableJson == null || tableJson.equals("")){
			return null;
		}
		//替换字符-"<br />" >> ""
		Pattern pattern = Pattern.compile("<br />|<br/>|<br>|<BR>|<BR/>|<BR />|<font>|</font>");
		Matcher matcher = pattern.matcher(tableJson);
		return matcher.replaceAll("");
		
	}
	
	/**
	 * 合并节点
	 *
	 * 
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private void mergeCell(){
		for(Integer[] oneMerge : mergeCells){
			CellRangeAddress address = new CellRangeAddress(oneMerge[0], oneMerge[1], oneMerge[2], oneMerge[3]);
			sheet.addMergedRegion(address);
			RegionUtil.setBorderBottom(1, address, sheet, wb);
			RegionUtil.setBorderLeft(1, address, sheet, wb);
			RegionUtil.setBorderRight(1, address, sheet, wb);
			RegionUtil.setBorderTop(1, address, sheet, wb);
		}
	}
	
	/**
	 * 调整列宽
	 *
	 * 
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private void autoCell(){
		for(int i = 0; i < colMax; i++){
			sheet.autoSizeColumn((short)i, true);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 5*256);
		}
	}
	
	/**
	 * 处理json格式数据生成excel,格式如下:<br/>
	 * "[[{'cols':3,'rows':4,'val':'3'},{'cols':3,'rows':4,'val':'3'}]]"
	 *
	 * 
	 * @param tableJson
	 * @param outputStream
	 * @throws Exception
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public void transJson2Excel(String tableJson, OutputStream outputStream)
		throws Exception{
		tableJson = handleTableJson(tableJson);
		ObjectMapper om = new ObjectMapper();
		ExcelTransTd[][] data = om.readValue(tableJson, ExcelTransTd[][].class);
		sheet = wb.createSheet();
		for(int j = 0; j < data.length; j++){
			ExcelTransTd[] rowData = data[j];
			row = sheet.createRow(rowIdx);
			cellIdx = 0;
			rowIdx++;
			for(int i = 0; i < rowData.length; i++){
				ExcelTransTd excelTransTd = rowData[i];
				while(mergeFlag.get("" + (rowIdx -1) + "," + cellIdx) != null){
					cellIdx++;
				}
				cell = row.createCell(cellIdx);
				cell.setCellValue(excelTransTd.getVal());
				cell.setCellStyle(cellStyle);
				int cols = excelTransTd.getCols();
				int rows = excelTransTd.getRows();
				//处理合并表头
				int beginRow = rowIdx -1;
				int endRow = rowIdx -1 + rows -1;
				int beginCol = cellIdx;
				int endCol = cellIdx + cols - 1;
				if(endRow != beginRow || endCol != beginCol){
					mergeCells.add(new Integer[]{beginRow, endRow, beginCol, endCol});
				}
				for(int m = beginRow; m<= endRow; m++){
					for(int n = beginCol; n <= endCol; n++){
						mergeFlag.put("" + m + "," + n, true);
					}
				}
				cellIdx = cellIdx + cols;
				colMax = cellIdx > colMax ? cellIdx : colMax;
			}
		}
		//合并单元格
		mergeCell();
		//调整列宽
		autoCell();
		wb.write(outputStream);
	}
	
	
}
