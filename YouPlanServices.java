package com.you.portal.youplans;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldAction;
import com.portal.pcm.fields.FldAmount;
import com.portal.pcm.fields.FldAmountOrig;
import com.portal.pcm.fields.FldAmountTaxed;
import com.portal.pcm.fields.FldCategory;
import com.portal.pcm.fields.FldCategoryType;
import com.portal.pcm.fields.FldCity;
import com.portal.pcm.fields.FldCompany;
import com.portal.pcm.fields.FldCreatedT;
import com.portal.pcm.fields.FldCreditFloor;
import com.portal.pcm.fields.FldCreditLimit;
import com.portal.pcm.fields.FldCreditThresholds;
import com.portal.pcm.fields.FldCycleDiscount;
import com.portal.pcm.fields.FldCycleEndCycle;
import com.portal.pcm.fields.FldCycleEndT;
import com.portal.pcm.fields.FldCycleStartCycle;
import com.portal.pcm.fields.FldCycleStartT;
import com.portal.pcm.fields.FldDays;
import com.portal.pcm.fields.FldDealName;
import com.portal.pcm.fields.FldDeals;
import com.portal.pcm.fields.FldDeliveryDescr;
import com.portal.pcm.fields.FldDescr;
import com.portal.pcm.fields.FldEndT;
import com.portal.pcm.fields.FldFixedAmount;
import com.portal.pcm.fields.FldFlags;
import com.portal.pcm.fields.FldHostname;
import com.portal.pcm.fields.FldLimit;
import com.portal.pcm.fields.FldMaxUpstreamBandwd;
import com.portal.pcm.fields.FldModT;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldNoteStr;
import com.portal.pcm.fields.FldPermitted;
import com.portal.pcm.fields.FldPlan;
import com.portal.pcm.fields.FldPlanObj;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldProdName;
import com.portal.pcm.fields.FldProducts;
import com.portal.pcm.fields.FldProgramName;
import com.portal.pcm.fields.FldProvisioningTag;
import com.portal.pcm.fields.FldPurchaseDiscount;
import com.portal.pcm.fields.FldPurchaseEndCycle;
import com.portal.pcm.fields.FldPurchaseEndT;
import com.portal.pcm.fields.FldPurchaseStartCycle;
import com.portal.pcm.fields.FldPurchaseStartT;
import com.portal.pcm.fields.FldQuantity;
import com.portal.pcm.fields.FldRatePlanSelectorObj;
import com.portal.pcm.fields.FldResult;
import com.portal.pcm.fields.FldScale;
import com.portal.pcm.fields.FldScaledAmount;
import com.portal.pcm.fields.FldSegmentName;
import com.portal.pcm.fields.FldServiceObj;
import com.portal.pcm.fields.FldServices;
import com.portal.pcm.fields.FldStartT;
import com.portal.pcm.fields.FldStatus;
import com.portal.pcm.fields.FldStatusFlags;
import com.portal.pcm.fields.FldStrVal;
import com.portal.pcm.fields.FldStringId;
import com.portal.pcm.fields.FldTax;
import com.portal.pcm.fields.FldTotalRecords;
import com.portal.pcm.fields.FldTypeStr;
import com.portal.pcm.fields.FldUintVal;
import com.portal.pcm.fields.FldUsageDiscount;
import com.portal.pcm.fields.FldUsageEndCycle;
import com.portal.pcm.fields.FldUsageEndT;
import com.portal.pcm.fields.FldUsageStartCycle;
import com.portal.pcm.fields.FldUsageStartT;
import com.you.portal.fields.PinFldIqaraDaysCf;
import com.you.portal.fields.PinFldIqaraGracePeriod;
import com.you.portal.fields.PinFldIqaraHrCf;
import com.you.portal.fields.PinFldIqaraMbCf;
import com.you.portal.fields.PinFldIqaraRcTag;
import com.you.portal.fields.PinFldIqaraResInvolved;
import com.you.vasp.PortlContextTran;
import com.youbb.api.exceptions.YOUBBAPIException;

import connectionpool.portal.ConnectionPoolManagerPortal;
import in.co.iqara.web.ErrorException;

public class YouPlanServices {

	private static final int PLAN_STATUS = 1;
	private static final int SERVICE_TAX = 18;
	private static final String IQ_PROV_TAGS_PROGRAME_NAME = "Iqara Provisioning Tags";
	private static final String LDAP_SUSP_POOL = "suspend";
	private static final int PLAN_FLAG = 1048576;
	private static final int DEAL_FLAG = 4194304;
	private static final int SERVICE_DEAL_POID = Integer.parseInt("-1");
	private static final int PORTAL_USER_POID = 2;
	private static final String PROGRAM_NAME = "YOU Auto Plan Creation";
	final static Logger log = (Logger) LoggerFactory.getLogger(YouPlanServices.class);
	static PortalContext ctx;
	static long database = 1;

	public static void main(String[] args, InputStream inputStream) {

		/*
		 * String filename =
		 * "c:\\Users\\vsaiyad\\git\\MobAppAPIs\\src\\main\\java\\com\\you\\portal\\youplans\\PlanExcel.xlsx";
		 */
		// YouPlanT youPlanT = new YouPlanT();

		List<YouPlanT> list = null;
		System.out.println("vasim debug 1111");
		try {
			list = YouPlanExcelRead.convertXLSXToListOfBean(inputStream);
			uploadPlanCreate(list);
		} catch (YOUBBAPIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * FOR BB PlanType will take input as below
		 * MBS,UNLTD,UNLTDDUAL,UNLTDVOICE,UNLTDDUALVOICE,HOUR For ESG : Will check later
		 * FOr Idea Plan unltd=IDEAUNLTD, for dual: IDEADUAL,For MB :IDEAMB
		 */

		// Based on this plan will create
		// youPlanT.setPlanType("UNLTDVOICE");
		// youPlanT.setBusinessType("1");
		// youPlanT.setArputAmt(830);
		// youPlanT.setPlanName("158_TEST/YOU COMBO VOICE SUPERNET
		// 2/2Mbps/3Months/Rs2938");
		// youPlanT.setPlanDesc("TEST158_YOU FALCON 100 GB 1 Month");
		// youPlanT.setSubscrAmt(2490);
		// youPlanT.setFreeDays(90);
		// youPlanT.setFreeMb(0);
		// youPlanT.setFreeHour(0);
		// youPlanT.setPlanCity("PUNE");
		// youPlanT.setPlanSegement("2 Mbps Plans");
		// youPlanT.setFinanceGroup("YOU COMBO VOICE SUPERNET 2");
		// youPlanT.setBandWidth(2050);
		// youPlanT.setDayBandwidth("");
		// youPlanT.setLowerBandwidth("");
		// youPlanT.setUploadBandwidth("");
		// youPlanT.setPlanStatus(1);
		// youPlanT.setProvTag("pool=unltdvoice");
		// youPlanT.setRcTag("UNLTD");
		// youPlanT.setMbLimit(0);
		// youPlanT.setResInvolved("1000101");
		// youPlanT.setPlanCategoryType("1");
		// youPlanT.setLdapActivePool("UNLTD");
		// youPlanT.setCompanyCodel("YOUBB");
		// youPlanT.setMbcf(0);
		// youPlanT.setHrcf(0);
		// youPlanT.setDaycf(0);

	}

	public static void uploadPlanCreate(List<YouPlanT> list) throws YOUBBAPIException {
		for (YouPlanT ypt : list) {
			log.info("Info:: {} ", ypt);
			YouPlanServices test = new YouPlanServices();

			try {
				test.createDealFlist(ypt);
				test.createPlanFlist(ypt);
				int planPoidId0 = test.findPlanPoidfromPlan(ypt.getPlanName());
				log.info("inside uploadPlanCreate Method");
				// TODO if planPoidId0 is null handle it
				ypt.setPlanPoidId0(planPoidId0);
				test.createIqProvEntryFlist(ypt);
			} catch (YOUBBAPIException | Exception ybb) {
				log.error(ybb.getMessage(), ybb);
			}
		}
	}

	private FList commitIqProvTagsEntry(FList in) throws YOUBBAPIException {
		PortlContextTran portconn = null;
		portconn = new PortlContextTran();
		FList outFlist = null;
		try {
			ctx = portconn.createConn();
			database = ctx.getCurrentDB();
			outFlist = ctx.opcode(1, in);
			log.info("Iq prov tags entry committed Successfully Database");
			log.info("Output Flist::", outFlist);
			return outFlist;
		} catch (Exception e) {
			log.error("Transaction Failed", e);
			throw new YOUBBAPIException("IQ Prov Tags commit Error", "COMMIT_IQ_PROV_TAGS_ERROR",
					"IQ Prov Tags commit Error");
		} finally {
			try {
				if (ctx != null)
					ctx.close(true);
			} catch (EBufException e) {
				// do nothing
			}
		}
	}

	private FList createBBAccountDeal(YouPlanT youplan) {
		System.out.println("Calling Create Deal Methid Vasim1111 :" + youplan.getPlanType());
		FList deal1 = new FList();
		deal1.set(FldPermitted.getInst(), "/account");
		deal1.set(FldName.getInst(), youplan.getPlanName());
		deal1.set(FldFlags.getInst(), DEAL_FLAG);
		deal1.set(FldDescr.getInst(), youplan.getPlanName());
		deal1.set(FldEndT.getInst(), new Date(0));
		deal1.set(FldStartT.getInst(), new Date(0));

		FList productSubscription;
		FList productDay;
		int j = 0;
		System.out.println("Plan Type Vasim111 :" + youplan.getPlanType());
		if (youplan.getPlanType().equals("UNLTDVOICE") || youplan.getPlanType().equals("MBS")
				|| youplan.getPlanType().equals("UNLTDDUAL") || youplan.getPlanType().equals("UNLTDDUALVOICE")
				|| youplan.getPlanType().equals("UNLTD")) {
			log.info("Day product Template Call");
			productDay = createBBDayProduct(new BigDecimal(youplan.getFreeDays()), youplan.getPlanType());
			deal1.setElement(FldProducts.getInst(), j, productDay);
			j++;
		}

		if (youplan.getPlanType().equals("MBS") || youplan.getPlanType().equals("UNLTDDUAL")
				|| youplan.getPlanType().equals("UNLTDDUALVOICE") || youplan.getPlanType().equals("IDEADUAL")
				|| youplan.getPlanType().equals("IDEAMB")) {
			FList productMB;
			productMB = createBBMBProduct(new BigDecimal(youplan.getFreeMb()), youplan.getPlanType());
			deal1.setElement(FldProducts.getInst(), j, productMB);
			j++;
		}

		productSubscription = createBBSubscritpionProduct(new BigDecimal(youplan.getSubscrAmt()),
				youplan.getPlanType());
		deal1.setElement(FldProducts.getInst(), j, productSubscription);
		j++;
		return deal1;
	}

	private FList createBBDayProduct(BigDecimal freeDay, String planType) {
		String productName = "";
		System.out.println("Inside DealBBDay Product Debug");
		if (planType.equals("MBS") || planType.equals("UNLTDVOICE") || planType.equals("UNLTDDUAL")
				|| planType.equals("UNLTDDUALVOICE") || planType.equals("UNLTD")) {
			productName = "Scalable Validity Period  (1 Day)";
		}
		return createDayProduct(freeDay, productName);
	}

	private FList createBBMBProduct(BigDecimal freeMB, String planType) {
		String productName = "";
		if (planType.equals("MBS") || planType.equals("UNLTDDUAL") || planType.equals("UNLTDDUALVOICE")) {
			productName = "Scalable MB Product (1MB)";
		} else if (planType.equals("IDEAMB") || planType.equals("IDEADUAL")) {
			productName = "Forward Postpaid Scalable MB Product (1MB)";
		}
		return createMbProductFlist(freeMB, productName);
	}

	private FList createDayProduct(BigDecimal freeDay, String productName) {
		FList productDay = new FList();
		productDay.set(FldPurchaseEndT.getInst(), new Date(0));
		productDay.set(FldPurchaseStartT.getInst(), new Date(0));
		productDay.set(FldQuantity.getInst(), freeDay);
		productDay.set(FldUsageEndCycle.getInst(), new BigDecimal(0));
		productDay.set(FldUsageStartCycle.getInst(), new BigDecimal(0));
		productDay.set(FldCycleEndCycle.getInst(), new BigDecimal(0));
		productDay.set(FldCycleStartCycle.getInst(), new BigDecimal(0));
		productDay.set(FldPurchaseEndCycle.getInst(), new BigDecimal(0));
		productDay.set(FldPurchaseStartCycle.getInst(), new BigDecimal(0));
		productDay.set(FldProdName.getInst(), productName);
		productDay.set(FldUsageDiscount.getInst(), new BigDecimal(0));
		productDay.set(FldCycleDiscount.getInst(), new BigDecimal(0));
		productDay.set(FldPurchaseDiscount.getInst(), new BigDecimal(0));
		productDay.set(FldStatus.getInst(), 1);
		productDay.set(FldStatusFlags.getInst(), 0);
		productDay.set(FldUsageEndT.getInst(), new Date(0));
		productDay.set(FldUsageStartT.getInst(), new Date(0));
		productDay.set(FldCycleEndT.getInst(), new Date(0));
		productDay.set(FldCycleStartT.getInst(), new Date(0));
		return productDay;
	}

	private FList createDeal(FList in) throws YOUBBAPIException {
		PortlContextTran portconn = null;
		String error1 = "Success";

		FList outFlist;
		try {
			portconn = new PortlContextTran();
			ctx = portconn.createConn();
			database = ctx.getCurrentDB();
			outFlist = ctx.opcode(660, in);
			log.info("Output FList for PCM_OP_PRICE_COMMIT_DEAL\n");
			log.info("OutPut FLIST := " + outFlist.toString());
			log.info("Result Success or Failed := " + outFlist.getField(FldResult.getInst()));
			int res = (int) outFlist.getField(FldResult.getInst());
			log.info("Result Field to check further Condition  " + res);

			if (res != 1) {
				throw new YOUBBAPIException("Deal Creation", "Deal creation failed", "Auto Deal Creation Failed");
			} else {
				log.info("Deal Created successfully");
			}
		} catch (Exception e) {
			log.error("The transaction result ::", error1);
			throw new YOUBBAPIException(e);
		} finally {
			try {
				if (ctx != null)
					ctx.close(true);
			} catch (EBufException e) {
				log.error("The transaction result ::", e);
			}
		}
		return outFlist;
	}

	public void createDealFlist(YouPlanT youplan) throws YOUBBAPIException {
		FList in = new FList();

		Poid dpoid = new Poid(database, PORTAL_USER_POID, "/service/admin_client");
		in.set(FldPoid.getInst(), dpoid);
		in.set(FldProgramName.getInst(), PROGRAM_NAME);
		FList deal1 = createBBAccountDeal(youplan);
		in.setElement(FldDeals.getInst(), 0, deal1);
		log.info("Input Deal FList::{}", in.toString());
		createDeal(in);
		// return in;
	}

	public FList createIqProvEntryFlist(YouPlanT ypt) throws YOUBBAPIException {
		Poid ipoid = new Poid(database, Integer.parseInt("-1"), "/config/iq_prov_tags");
		// Poid apoid = new Poid(database, Integer.parseInt("1"), "/account");
		Poid vppoid = new Poid(database, Integer.parseInt("0"), "/plan");

		log.info("PlanPoidId0 :==  ", ypt.getPlanPoidId0());
		Poid ppoid = new Poid(database, (ypt.getPlanPoidId0()), "/plan");
		FList in = new FList();
		FList plan1 = new FList();

		in.set(FldPoid.getInst(), ipoid);
		in.set(FldProgramName.getInst(), IQ_PROV_TAGS_PROGRAME_NAME);
		in.set(FldName.getInst(), IQ_PROV_TAGS_PROGRAME_NAME);
		in.set(FldHostname.getInst(), "--");

		plan1.set(FldPoid.getInst(), ppoid);
		plan1.set(FldProvisioningTag.getInst(), ypt.getProvTag());
		plan1.set(FldDescr.getInst(), ypt.getFinanceGroup());
		plan1.set(FldAmount.getInst(), new BigDecimal(ypt.getSubscrAmt()));
		plan1.set(FldTax.getInst(), new BigDecimal(SERVICE_TAX));
		plan1.set(PinFldIqaraDaysCf.getInst(), ypt.getDaycf());
		plan1.set(PinFldIqaraHrCf.getInst(), ypt.getHrcf());
		plan1.set(PinFldIqaraRcTag.getInst(), ypt.getRcTag());
		plan1.set(PinFldIqaraResInvolved.getInst(), ypt.getResInvolved());
		plan1.set(PinFldIqaraGracePeriod.getInst(), 0);
		plan1.set(PinFldIqaraMbCf.getInst(), ypt.getMbcf());

		plan1.set(FldFixedAmount.getInst(), new BigDecimal(0));
		plan1.set(FldScaledAmount.getInst(), new BigDecimal(0));
		plan1.set(FldSegmentName.getInst(), ypt.getPlanSegement());

		System.out.println("Day Bandwidth : " + ypt.getDayBandwidth().isEmpty());
		System.out.println("Day Bandwidth : " + ypt.getLowerBandwidth().isEmpty());

		plan1.set(FldAction.getInst(), ypt.getDayBandwidth() != null ? ypt.getDayBandwidth() : "");
		plan1.set(FldStrVal.getInst(), ypt.getLowerBandwidth() != null ? ypt.getLowerBandwidth() : "");

		plan1.set(FldTotalRecords.getInst(), ypt.getMbLimit());
		plan1.set(FldMaxUpstreamBandwd.getInst(), 0);

		plan1.set(FldCategoryType.getInst(), Integer.parseInt(ypt.getPlanCategoryType()));
		plan1.set(FldStringId.getInst(), ypt.getBandWidth());
		plan1.set(FldQuantity.getInst(), new BigDecimal(ypt.getFreeMb()));

		if (ypt.getPlanType().equals("UNLTDVOICE")) {
			plan1.set(FldTypeStr.getInst(), "UNLTD");
		} else if (ypt.getPlanType().equals("IDEAUNLTD")) {
			plan1.set(FldTypeStr.getInst(), "PPFWDUNLTD");
		} else if (ypt.getPlanType().equals("IDEADUAL") || ypt.getPlanType().equals("IDEAMB")) {
			plan1.set(FldTypeStr.getInst(), "PPFWDMB");
		} else if (ypt.getPlanType().equals("UNLTDDUALVOICE") || ypt.getPlanType().equals("UNLTDDUAL")) {
			plan1.set(FldTypeStr.getInst(), "MBS");
		} else {
			plan1.set(FldTypeStr.getInst(), ypt.getPlanType());
		}

		plan1.set(FldCategory.getInst(), LDAP_SUSP_POOL);
		plan1.set(FldDeliveryDescr.getInst(), ypt.getLdapActivePool());
		plan1.set(FldAmountOrig.getInst(), new BigDecimal(ypt.getArputAmt()));
		plan1.set(FldAmountTaxed.getInst(), new BigDecimal(0));
		plan1.set(FldScale.getInst(), new BigDecimal(0));
		plan1.set(FldDays.getInst(), (ypt.getFreeDays()));
		plan1.set(FldPlanObj.getInst(), vppoid);
		plan1.set(FldRatePlanSelectorObj.getInst(), vppoid);
		plan1.set(FldNoteStr.getInst(), ypt.getFinanceGroup());
		plan1.set(FldCity.getInst(), ypt.getPlanCity());
		plan1.set(FldStatus.getInst(), PLAN_STATUS);
		plan1.set(FldUintVal.getInst(), 0);
		plan1.set(FldCompany.getInst(), ypt.getCompanyCode());

		in.setElement(FldPlan.getInst(), 0, plan1);

		log.info("Input IQ Prov Tag FList::{}", in.toString());
		commitIqProvTagsEntry(in);
		return in;

	}

	private FList createBBSubscritpionProduct(BigDecimal subAmt, String planType) {
		String productName = "";
		if (planType.equals("MBS")) {
			productName = "Iqara - Subscription Fee 1 Scalable (41103)";
		} else if (planType.equals("UNLTD") || planType.equals("UNLTDDUAL")) {
			productName = "Iqara - Subscription Fee 1 Scalable(43105)";
		} else if (planType.equals("HOUR")) {
			productName = "Iqara - Subscription Fee 1 Scalable (46042)";
		} else if (planType.equals("UNLTDVOICE") || planType.equals("UNLTDDUALVOICE")) {
			productName = "Iqara - Data Subscription Fee 1 Scalable(43108)";
		} else if (planType.equals("IDEAMB") || planType.equals("IDEAUNLTD") || (planType.equals("IDEADUAL"))) {
			productName = "Forward Postpaid Service Monthly Cycle Rupees Scalable(61211)";
		}
		return createSubProduct(subAmt, productName);
	}

	private FList createMbProductFlist(BigDecimal freeMB, String productName) {
		FList productMB = new FList();
		productMB.set(FldPurchaseEndT.getInst(), new Date(0));
		productMB.set(FldPurchaseStartT.getInst(), new Date(0));
		productMB.set(FldQuantity.getInst(), freeMB);
		productMB.set(FldUsageEndCycle.getInst(), new BigDecimal(0));
		productMB.set(FldUsageStartCycle.getInst(), new BigDecimal(0));
		productMB.set(FldCycleEndCycle.getInst(), new BigDecimal(0));
		productMB.set(FldCycleStartCycle.getInst(), new BigDecimal(0));
		productMB.set(FldPurchaseEndCycle.getInst(), new BigDecimal(0));
		productMB.set(FldPurchaseStartCycle.getInst(), new BigDecimal(0));
		productMB.set(FldProdName.getInst(), productName);
		productMB.set(FldUsageDiscount.getInst(), new BigDecimal(0));
		productMB.set(FldCycleDiscount.getInst(), new BigDecimal(0));
		productMB.set(FldPurchaseDiscount.getInst(), new BigDecimal(0));
		productMB.set(FldStatus.getInst(), 1);
		productMB.set(FldStatusFlags.getInst(), 0);
		productMB.set(FldUsageEndT.getInst(), new Date(0));
		productMB.set(FldUsageStartT.getInst(), new Date(0));
		productMB.set(FldCycleEndT.getInst(), new Date(0));
		productMB.set(FldCycleStartT.getInst(), new Date(0));
		return productMB;
	}

	private FList createPlan(FList inp) throws YOUBBAPIException {
		PortlContextTran portconn = null;
		String error1 = "Success";
		try {
			portconn = new PortlContextTran();
			ctx = portconn.createConn();
			database = ctx.getCurrentDB();
		} catch (EBufException e) {
			log.error("Error: Cant connect to Portal");
			throw new YOUBBAPIException("Plan Creation ", " Plan Creation Failed", "Auto Plan Creation Failed");
		} catch (ErrorException h) {
			error1 = "Error: Cant connect to Portal";
			h.printStackTrace();
		}
		FList outFlist = null;
		try {
			outFlist = ctx.opcode(659, inp);
			log.info("Output FList for PCM_OP_PRICE_COMMIT_PLAN\n");
			log.info("PLAN OutPut FLIST := " + outFlist.toString());
			log.info("Result Success or Failed := " + outFlist.getField(FldResult.getInst()));
			int res = (int) outFlist.getField(FldResult.getInst());
			if (res != 1) {
				error1 = "Error: Transaction Failed";
				log.info("The transaction result ::" + error1);
				return outFlist;
			} else {
				log.info("Plan Created successfully");
			}
		} catch (Exception e) {
			log.error("The transaction result ::", e);
			return outFlist;
		} finally {
			try {
				if (ctx != null)
					ctx.close(true);
			} catch (EBufException e) {
				e.printStackTrace();
			}
		}
		return outFlist;
	}

	public void createPlanFlist(YouPlanT youplan) throws YOUBBAPIException {
		Poid ppoid = new Poid(database, PORTAL_USER_POID, "/service/admin_client");
		Poid spoid = new Poid(database, SERVICE_DEAL_POID, "/service/ip");

		FList inp = new FList();
		FList plan = new FList();
		FList dayLimit1 = new FList();
		FList mbLimit2 = new FList();
		FList service = new FList();
		Date dt = new Date();
		long tstamp = dt.getTime();

		inp.set(FldPoid.getInst(), ppoid);
		inp.set(FldProgramName.getInst(), PROGRAM_NAME);

		plan.set(FldName.getInst(), youplan.getPlanName());
		plan.set(FldFlags.getInst(), PLAN_FLAG);
		plan.set(FldModT.getInst(), new Date(tstamp));
		plan.set(FldCreatedT.getInst(), new Date(tstamp));
		plan.set(FldDealName.getInst(), youplan.getPlanName());
		plan.set(FldDescr.getInst(), youplan.getPlanDesc());
		inp.setElement(FldPlan.getInst(), 0, plan);

		dayLimit1.set(FldCreditLimit.getInst(), new BigDecimal(0));
		dayLimit1.set(FldCreditThresholds.getInst(), 0);
		dayLimit1.set(FldCreditFloor.getInst(), new BigDecimal(youplan.getFreeDays()).negate());
		plan.setElement(FldLimit.getInst(), 1000101, dayLimit1);

		if (youplan.getPlanType().equals("MBS") || youplan.getPlanType().equals("UNLTDDUAL")
				|| youplan.getPlanType().equals("UNLTDDUALVOICE") || youplan.getPlanType().equals("IDEADUAL")
				|| youplan.getPlanType().equals("IDEAMB")) {
			log.info("MB Plan Template Call");
			mbLimit2.set(FldCreditLimit.getInst(), new BigDecimal(0));
			mbLimit2.set(FldCreditThresholds.getInst(), 0);
			mbLimit2.set(FldCreditFloor.getInst(), new BigDecimal(youplan.getFreeMb()).negate());
			plan.setElement(FldLimit.getInst(), 1000050, mbLimit2);
		}
		service.set(FldServiceObj.getInst(), spoid);
		createServiceLevelDeal(youplan, service);
		plan.setElement(FldServices.getInst(), 0, service);
		log.info("Input FList::{}", inp.toString());
		createPlan(inp);
	}

	private void createServiceLevelDeal(YouPlanT youplan, FList service) {
		if (youplan.getPlanType().equals("MBS") || youplan.getPlanType().equals("UNLTDDUAL")
				|| youplan.getPlanType().equals("UNLTDDUALVOICE")) {
			service.set(FldDealName.getInst(), "Iqara service rating (Full MB Rating)");
		} else if (youplan.getPlanType().equals("IDEADUAL") || youplan.getPlanType().equals("IDEAMB")) {
			service.set(FldDealName.getInst(), "Idea service rating (Full MB Rating)");
		} else if (youplan.getPlanType().equals("IDEAUNLTD")) {
			service.set(FldDealName.getInst(), "Idea => Delight Service Rating");
		} else if (youplan.getPlanType().equals("UNLTD") || youplan.getPlanType().equals("UNLTDVOICE")) {
			service.set(FldDealName.getInst(), "IQARA => Delight Service Rating");
		} else if (youplan.getPlanType().equals("HOUR")) {
			service.set(FldDealName.getInst(), "Technet Migration (Hours)");
		}
	}

	private FList createSubProduct(BigDecimal quantity, String ProductName) {
		FList productSubscription = new FList();
		productSubscription.set(FldPurchaseEndT.getInst(), new Date(0));
		productSubscription.set(FldProdName.getInst(), ProductName);
		productSubscription.set(FldPurchaseStartT.getInst(), new Date(0));
		productSubscription.set(FldQuantity.getInst(), quantity);
		productSubscription.set(FldUsageEndCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldUsageStartCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldCycleEndCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldCycleStartCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldPurchaseEndCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldPurchaseStartCycle.getInst(), new BigDecimal(0));
		productSubscription.set(FldUsageDiscount.getInst(), new BigDecimal(0));
		productSubscription.set(FldCycleDiscount.getInst(), new BigDecimal(0));
		productSubscription.set(FldPurchaseDiscount.getInst(), new BigDecimal(0));
		productSubscription.set(FldStatus.getInst(), 1);
		productSubscription.set(FldStatusFlags.getInst(), 0);
		productSubscription.set(FldUsageEndT.getInst(), new Date(0));
		productSubscription.set(FldUsageStartT.getInst(), new Date(0));
		productSubscription.set(FldCycleEndT.getInst(), new Date(0));
		productSubscription.set(FldCycleStartT.getInst(), new Date(0));
		return productSubscription;
	}

	public int findPlanPoidfromPlan(String planName) {
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		int poidId = 0;

		try {
			conn = ConnectionPoolManagerPortal.getInstance().getConnection();
			conn.setAutoCommit(false);

			s = conn.createStatement();
			String query = "select poid_id0 from plan_t where name=?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, planName);
			rs = ps.executeQuery();

			while (rs.next()) {
				poidId = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Error IN Databaase" + e.getMessage());

		} finally {
			try {
				if (s != null)
					s.close();
			} catch (Exception e) {
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			if (conn != null)
				ConnectionPoolManagerPortal.getInstance().releaseConnection(conn);
		}
		log.info("Plan Poid_id0 :==" + poidId);
		return poidId;
	}

}
