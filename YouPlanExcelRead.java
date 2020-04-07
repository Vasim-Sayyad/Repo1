package com.you.portal.youplans;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youbb.api.exceptions.YOUBBAPIException;

public class YouPlanExcelRead {

	final static Logger log = (Logger) LoggerFactory.getLogger(YouPlanExcelRead.class);
	private static final InputStream InputStream1 = null;
	static String regex = "^[a-zA-Z0-9 / _ -]+$";
	static String regex_city = "^[a-zA-Z / _ ,]+$";
	static String regexNumber = "[+]?[0-9][0-9]*";
	private static Pattern Ptrn = Pattern.compile(regex);
	private static Pattern Ptrn_city = Pattern.compile(regex_city);
	private static Pattern NumberPtrn = Pattern.compile(regexNumber);

	public static List<YouPlanT> convertXLSXToListOfBean(InputStream inputStream) throws YOUBBAPIException {
		List<YouPlanT> list = new ArrayList<YouPlanT>(0);
		XSSFWorkbook myWorkbook = null;

		try {
			myWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet mySheet = myWorkbook.getSheetAt(0);
			Iterator<Row> rowIterator = mySheet.iterator();
			log.info("Total No of rows in sheet  = " + mySheet.getLastRowNum());

			while (rowIterator.hasNext()) {
				String[] material = new String[13];
				Row row = rowIterator.next();
				log.info("Current Row No: " + row.getRowNum());
				if (row.getRowNum() == 0) {
					log.info("\t in row checking");
					continue;
				}
				YouPlanT yplant = null;
				yplant = new YouPlanT();
				try {
					if (row.getLastCellNum() > 13 || row.getLastCellNum() < 13) {
						log.info("Excel Total No of Cell Length is:= " + row.getLastCellNum());
						throw new YOUBBAPIException("TOTAL NO CELL IS LESS", "MISSING_CELL_VALUES",
								"TOTAL NO CELL IS LESS");
					}
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						DataFormatter dataFormatter = new DataFormatter();
						material[cell.getColumnIndex()] = dataFormatter.formatCellValue(cell);
					}

					int count = 0;
					for (String s : material) {
						count++;
						log.info("Column no : " + count + "\t value : " + s);
					}
					yplant = validateConvertToBean(material);
					list.add(yplant);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (myWorkbook != null) {
				try {
					myWorkbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	// Uncomment below once we start to upload from JSP front-end

	public static void main(String[] args, InputStream InputStream) throws YOUBBAPIException {
		/*
		 * try { // File inFile = new File( // //
		 * "c:\\Users\\vsaiyad\\git\\MobAppAPIs\\src\\main\\java\\com\\you\\portal\\youplans\\PlanExcel.xlsx"
		 * // ); FileInputStream InputStream1 = new FileInputStream(inFile);// ERROR //
		 * //convertXLSXToListOfBean(InputStream1); // log.info("Inpputstream1" +
		 * InputStream1); // } catch (FileNotFoundException fe) { fe.printStackTrace();
		 * }
		 */
		convertXLSXToListOfBean(InputStream1);
		log.info("Inpputstream" + InputStream);
	}

	// Below is for localtest once we start upload from jSP please comment below
	// code
	/*
	 * public static void main(String[] args) throws YOUBBAPIException { try { File
	 * inFile = new File(
	 * "c:\\Users\\vsaiyad\\git\\MobAppAPIs\\src\\main\\java\\com\\you\\portal\\youplans\\MyFirstExcel.xlsx"
	 * ); FileInputStream InputStream1 = new FileInputStream(inFile);// ERROR
	 * convertXLSXToListOfBean(InputStream1); log.info("Inpputstream1" +
	 * InputStream1); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
	 * convertXLSXToListOfBean(InputStream1); log.info("Inpputstream" +
	 * InputStream1); }
	 */
	private static YouPlanT validateConvertToBean(String[] material) throws YOUBBAPIException {
		YouPlanT yplant = new YouPlanT();

		if (material.length < 0) {
			throw new YOUBBAPIException("Invalid File values length", "INVALID_FILE_VALUE_LENGTH",
					"Invalid File values length");
		}

		if (null == material[0] || "".equals(material[0].trim())) {
		} else {
			if (!(material[0].equals("MBS") || material[0].equals("UNLTD") || material[0].equals("UNLTDDUAL")
					|| material[0].equals("UNLTDVOICE") || material[0].equals("UNLTDDUALVOICE")
					|| material[0].equals("HOUR") || material[0].equals("IDEAUNLTD") || material[0].equals("IDEADUAL")
					|| material[0].equals("IDEAMB"))) {
				throw new YOUBBAPIException(
						"Plan type must be MBS,UNLTD,UNLTDDUAL,UNLTDVOICE,UNLTDDUALVOICE,HOUR for YBB and IDEAUNLTD,IDEADUAL,IDEAMB for IDEA",
						"PLAN_TYPE_ERROR",
						"Plan type must be MBS,UNLTD,UNLTDDUAL,UNLTDVOICE,UNLTDDUALVOICE,HOUR for YBB and IDEAUNLTD,IDEADUAL,IDEAMB for IDEA");
			}
		}

		if (null == material[1] || "".equals(material[1].trim())) {
			throw new YOUBBAPIException("planName found blank", "EMPTY", "planName found blank");
		} else if (!(Ptrn.matcher(material[1]).matches())) {
			throw new YOUBBAPIException("Invalid Plan Name", "INVALID", "Invalid Plan Name");
		}

		if (null == material[2] || "".equals(material[2].trim())) {
			throw new YOUBBAPIException("planDesc found blank", "EMPTY", "planDesc found blank");
		} else if (!(Ptrn.matcher(material[2]).matches())) {
			throw new YOUBBAPIException("Invalid Plan Desc", "INVALID", "Invalid Plan Desc");
		}

		if (null == material[3] || "".equals(material[3].trim())) {
			throw new YOUBBAPIException("subscrAmt found blank", "EMPTY", "subscrAmt found blank");
		} else if (!(NumberPtrn.matcher(material[3]).matches())) {
			throw new YOUBBAPIException("Invalid Subscr Amount", "INVALID", "Invalid Subscr Amount");
		}

		if (null == material[4] || "".equals(material[4].trim())) {
			throw new YOUBBAPIException("arputAmt found blank", "EMPTY", "arputAmt found blank");
		} else if (!(NumberPtrn.matcher(material[4]).matches())) {
			throw new YOUBBAPIException("Invalid Arpu Amount", "INVALID", "Invalid Arpu Amount");
		}

		if (null == material[5] || "".equals(material[5].trim())) {
			throw new YOUBBAPIException("freeMb found blank", "EMPTY", "freeMb found blank");
		} else if (!(NumberPtrn.matcher(material[5]).matches())) {
			throw new YOUBBAPIException("Invalid FreeMB", "INVALID", "Invalid FreeMB");
		}

		if ((!(material[0].equals("UNLTD") || material[0].equals("UNLTDVOICE"))) && material[5].equals("0")) {
			throw new YOUBBAPIException("FREE MB ZERO FOR UNLTD PLANS", "FREEMB_INVALID_UNLTD",
					"FREE MB ZERO FOR UNLTD PLANS");
		}

		if (null == material[6] || "".equals(material[6].trim())) {
			throw new YOUBBAPIException("freeDays found blank", "EMPTY", "freeDays found blank");
		} else if (!(NumberPtrn.matcher(material[6]).matches())) {
			throw new YOUBBAPIException("Invalid Free Day", "INVALID", "Invalid Free Day");
		} else if (!(Integer.parseInt(material[6]) > 0 && Integer.parseInt(material[6]) < 1000)) {
			throw new YOUBBAPIException("Free Day Must be Greater than ZERO and less to 1000", "FREE_DAY_EXCEED_LIMIT",
					"Free Day Must be Greater than ZERO and less to 1000");
		}

		if (null == material[7] || "".equals(material[7].trim())) {
			throw new YOUBBAPIException("bandWidth found blank", "EMPTY", "bandWidth found blank");
		} else if (!(NumberPtrn.matcher(material[7]).matches())) {
			throw new YOUBBAPIException("Invalid Bandwidth", "INVALID", "Invalid Bandwidth");
		}

		if (!(null == material[8] || "".equals(material[8].trim())) && (!(material[0].equals("UNLTDDUALVOICE")
				|| material[0].equals("UNLTDDUAL") || material[0].equals("IDEADUAL")))) {
			throw new YOUBBAPIException("Invalid Lower Bandwidth", "INVALID", "Invalid Lower Bandwidth for this Plan");
		}

		if (null == material[9] || "".equals(material[9].trim())) {
			throw new YOUBBAPIException("planCity found blank", "EMPTY", "planCity found blank");
		} else if (!(Ptrn_city.matcher(material[9]).matches())) {
			throw new YOUBBAPIException("Invalid plan City", "INVALID", "Invalid plan City");
		}

		if (null == material[10] || "".equals(material[10].trim())) {
			throw new YOUBBAPIException("financeGroup found blank", "EMPTY", "financeGroup	found blank");
		} else if (!(Ptrn.matcher(material[10]).matches())) {
			throw new YOUBBAPIException("Invalid Finance Group", "INVALID", "Invalid Finance Group");
		}

		if (null == material[11] || "".equals(material[11].trim())) {
			throw new YOUBBAPIException("planSegement found blank", "EMPTY", "planSegementfound blank");
		} else if (!(Ptrn.matcher(material[11]).matches())) {
			throw new YOUBBAPIException("Invalid Plan Segment", "INVALID", "Invalid Plan Segment");
		}

		if (null == material[12] || "".equals(material[12].trim())) {
			throw new YOUBBAPIException("PlanCategory Found Blank", "EMPTY", "PlanCategory Found Blank");
		} else if (!((material[12]).equals("Gold") || material[12].equals("Silver")
				|| material[12].equals("Platinum"))) {
			throw new YOUBBAPIException("Invalid PlanCategory", "INVALID", "Invalid PlanCategory");
		}

		yplant.setBusinessType("1");
		yplant.setPlanType(material[0]);
		yplant.setPlanName(material[1]);
		yplant.setPlanDesc(material[2]);

		if (material[0].equals("MBS") || material[0].equals("UNLTDDUAL") || material[0].equals("UNLTDDUALVOICE")) {
			yplant.setRcTag("MBNOCF");
		} else if (material[0].equals("UNLTD") || material[0].equals("UNLTDVOICE")) {
			yplant.setRcTag("UNLTD");
		} else if (material[0].equals("IDEADUAL") || material[0].equals("IDEAMB")) {
			yplant.setRcTag("PPFWDMB");
		} else if (material[0].equals("IDEAUNLTD")) {
			yplant.setRcTag("PPFWDUNLTD");
		} else {
			throw new YOUBBAPIException("RC_TAG_ERROR", "INVALID", "RC_TAG_ERROR");
		}

		if (material[0].equals("MBS") || material[0].equals("IDEAMB")) {
			yplant.setProvTag("pool=mbsncf");
		} else if (material[0].equals("UNLTD") || material[0].equals("IDEAUNLTD")) {
			yplant.setProvTag("pool=unltd");
		} else if (material[0].equals("UNLTDDUAL") || material[0].equals("IDEADUAL")) {
			yplant.setProvTag("pool=unltddual");
		} else if (material[0].equals("UNLTDVOICE")) {
			yplant.setProvTag("pool=unltdvoice");
		} else if (material[0].equals("UNLTDDUALVOICE")) {
			yplant.setProvTag("pool=unltddualvoice");
		} else {
			throw new YOUBBAPIException("PROV_TAG_ERROR", "INVALID", "PROV_TAG_ERROR");
		}

		yplant.setSubscrAmt(Integer.parseInt(material[3]));
		yplant.setArputAmt(Integer.parseInt(material[4]));
		yplant.setFreeMb(Integer.parseInt(material[5]));
		yplant.setFreeDays(Integer.parseInt(material[6]));

		yplant.setFreeHour(0);
		yplant.setBandWidth(Integer.parseInt(material[7]));
		yplant.setLowerBandwidth(material[8] != null ? material[8] : "");
		yplant.setDayBandwidth("");
		yplant.setPlanCity(material[9]);

		yplant.setPlanStatus(1);
		yplant.setFinanceGroup(material[10]);
		yplant.setPlanSegement(material[11]);

		yplant.setUploadBandwidth("0");
		if (!(material[0].equals("UNLTDDUALVOICE") || material[0].equals("UNLTDDUAL") || material[0].equals("IDEAUNLTD")
				|| material[0].equals("IDEADUAL") || material[0].equals("MBS"))) {
			yplant.setMbLimit(0);
		} else {
			yplant.setMbLimit(Integer.parseInt(material[5]));
		}

		if (material[1].equals("MBS")) {
			yplant.setResInvolved("1000050-1000101");
		} else {
			yplant.setResInvolved("1000101");
		}

		if (material[12].equalsIgnoreCase("Gold")) {
			yplant.setPlanCategoryType("102");
		} else if (material[12].equalsIgnoreCase("Platinum")) {
			yplant.setPlanCategoryType("101");
		} else if (material[12].equalsIgnoreCase("Silver")) {
			yplant.setPlanCategoryType("103");
		}

		if (material[0].equals("MBS")) {
			yplant.setLdapActivePool("default");
		} else {
			yplant.setLdapActivePool("UNLTD");
		}

		if (material[0].equals("IDEADUAL") || material[0].equals("IDEAUNLTD") || material[0].equals("IDEAMB")) {
			yplant.setCompanyCodel("IDEA");
		} else {
			yplant.setCompanyCodel("YOUBB");
		}
		yplant.setMbcf(0);
		yplant.setHrcf(0);
		yplant.setDaycf(0);

		log.info("Excel after setting bean info {} :: " + yplant);
		return yplant;
	}

}
