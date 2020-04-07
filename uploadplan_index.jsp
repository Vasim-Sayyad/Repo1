<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="error.jsp" import="iqara.metasolv.web.userAccess,java.util.*,java.lang.*,java.math.*,java.util.Date,java.text.SimpleDateFormat" %>
<%
if(session.getAttribute("login") != null && (!session.getAttribute("login").equals("")))
{
	String uid = (String)session.getAttribute("login");
	Vector rights=(Vector) session.getAttribute("rights");
	String ct=(String)session.getAttribute("ct");

	if(!rights.contains("114"))
	{
		%>
		<jsp:forward page="../menu.jsp">	
		<jsp:param name="error" value="You have no rights to see this page" />	
		</jsp:forward>
		<%
	}
	//https://itstaging01.youbroadband.in/default/gstin/index.jsp
	Date dNow = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	String datetime = ft.format(dNow);
	String syncToken=uid+","+datetime;
	session.setAttribute("syncToken", syncToken);
	System.out.println("syncToken-->"+(String)session.getAttribute("syncToken"));

	%>
	<HTML><HEAD><TITLE>YOU Broadband India Ltd.</TITLE>
	<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
	<META content="MSHTML 6.00.2600.0" name=GENERATOR>
	<LINK href="styles/style.css" type=text/css rel=stylesheet>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
	<link href="styles/style.css" rel="stylesheet" type="text/css">
	<style>
	.input {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	height: 20px;
	border: 1px inset #91B5C5;
	background-color: #EDF5FA;
	padding: 3px;
	}

	td {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	}

	label {
		font-weight: bold;
	}

	.td1 {
		background-color: #91b5c5;
	}

	.td2 {
		background-color: #bad9eb;
	}

	.td3 {
		background-color: #EDF5FA;
	}

	.td4 {
		background-color: #edf5fa;
	}

	a {
		text-decoration: none;
	font-weight: bold;
	}

	a:hover {
		text-decoration: underline;
	font-weight: bold;
	}

	body {

	}

	li {
		line-height: 25px;
	list-style-type: square;
	}

	.input {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	height: 20px;
	border: 1px inset #91B5C5;
	background-color: #EDF5FA;
	padding: 3px;
	}

	.button {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	height: 22px;
	background-color: #C9E0F0;
	padding: 2px;
	font-weight: bold;
	clear: both;
	border: 1px ridge #D0E3F1;
	}

	.small {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	}

	.mess {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	color: #FF6600;
	text-decoration: bold;
	}

	.errmess {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: darkred;
	text-decoration: bold;
	}

	.dropDown {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	height: 25px;
	background-color: #EDF5FA;
	border: 1px solid #D0E3F1;
	padding: 4px;
	}

	.textArea {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	height: 100px;
	background-color: #EDF5FA;
	border: 1px outset #D0E3F1;
	padding: 4px;
	}

	.link {
		text-decoration: underline;
	}

	.link:hover {
		text-decoration: none;
	}

	.t {
		border: 1px outset;
	}

	.t2 {
		border: 1px ridge;
	}

	.tblhead {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 13px;
	color: black;
	text-decoration: bold;
	}

	video {
		border: 1px dashed gray;
	}
	</style>
	<script type="text/javascript">
	function validateForm() {
		var filename = document.getElementById("issueFile").value;
		if(filename === ""){
			alert("No file selected.");
			return false;
		}

		var allowedExtension = new Array("xlsx","xls");
		var fileExtension = filename.split(".").pop();
		var extFlag = 0;
		for(var i = 0; i <= allowedExtension.length; i++) {
			if( allowedExtension[i] == fileExtension ){
				extFlag = 1;
			}
		}
		if(extFlag == 0) {
			alert("Only xls and xlsx files are allowed");
			return false;
		}

		if (confirm("Are you sure do you want upload file ?")) {
			document.getElementById('submit1').disabled = 'disable';
			document.getElementById("divMsg").style.display = "";
			document.forms[0].submit();
			return true;
		} else {
			return false;
		}
	}
	function validdbclick()
	{
		alert("Only single Click Allowed!");
		return false;
	}

	</script>
	</HEAD>


	<BODY text=#002b3c vLink=#002b3c aLink=#002b3c link=#002b3c bgColor=#edf5fa
	leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" >

	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>

	<TR>
	<TD vAlign=top align=middle bgColor=#ddebf6 height=94>
	<TABLE cellSpacing=0 cellPadding=0 width=772 border=0>

	<TR>
	<TD width=772>
	<TABLE cellSpacing=0 cellPadding=0 width=772 border=0>

	<TR>
	<TD width=180><IMG height=94 src="../images/logo.gif"
	width=208></TD>
	<TD vAlign=top align=left width=592>
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>

	<TR>
	<TD>
	<TABLE cellSpacing=0 cellPadding=0 width="100%"
	background=images/img_1_2.jpg border=0>

	<TR>
	<TD vAlign=center align=middle height=71>&nbsp;
	</TD></TR></TABLE></TD></TR>
	<TR>
	<TD>
	<TABLE cellSpacing=0 cellPadding=0 width="100%"
	background=../images/strip_menu.jpg border=0>

	<TR>
	<TD vAlign=center align=left>&nbsp;</TD>
	<TD vAlign=center align=right height=23><a href=../menu.jsp>Menu</a>&nbsp;&nbsp;&nbsp;
	<a href=../logout.jsp>Logout</a>                            
	<!-- <h5><div align="right"><a href="../uploadplan_index.jsp">Upload Again</a></div></h5> -->
	</TD></TR></TABLE></TD></TR></TABLE></TD></TR></TABLE></TD></TR></TABLE>              

	<div align="center" class="td1"><BR>
	<B>Auto Plan creation</B>
	<br>
	<br>
	</div>
	</TD>
	</TR></TABLE>
	</br></br>
	<TABLE width=50% border=0 cellPadding=0 cellSpacing=0 class="td1" align="center">

	<TR>
	<TD>
	<TABLE cellSpacing=1 cellPadding=0 width=100% border=0>

	<TR>

	<TD class="td4">
	<%
	if(ct==null)
	{
		%>
		<div align="center"><strong>Welcome <font color="#FF0000"><%=uid%> 
		</font>.<br>
		Your city information is not found.<br>So you are not eligible to view any reports. Please contact your immediate superior. </font> </strong><br>
		<br>
		</div>
		<%
	}
	else
	{
		%>

		<div align="center"><strong>Welcome <font color="#FF0000"><%=uid%> 
		</font>.<br>
		You have access to reports for <font color="#FF0000"><%=ct%> </font>City</strong><br>
		<br>
		</div>
		<%
	}
	%>	
	<form action="uploadplan_action.jsp" method="post" enctype="multipart/form-data">
	<table width="40%" height="337%" border="0" align="center">
	<tr>
	<td align="left" ><b>Upload&nbsp;&nbsp;File:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
	<td align="left">
	<input type="file" name="issueFile" id="issueFile" enctype="multipart/form-data" size="50" />
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td  align="left">
	<input type="submit" value="Upload File" id="submit1" 
	onclick="return validateForm(); this.disabled=true;" ondblclick="return validdbclick();" />
	</td>	
	</tr>
	<tr><td colspan="2" align="center"><div id="divMsg" align="center" style="display: none;">
	<img src="../images/ajax-loader.gif"
	alt="Please wait,while your request is being processed.Do not refresh page or back button whiel plan is in process.." />
	</div></td></tr>
	<tr><td><input type="hidden" name="uniqn" id="uniq"  value="<%=syncToken%>"  ></td><tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
	<br></br>
	<tr>
	<td colspan="2" align="left">Note: Please verify data before uploading file.Please do not refresh while processing your request</TD>
	</TR>
	<br><br>
	</form>
	</td>
	</tr>
	<br><br>
	<tr align="left">
	<br><br><br>
	<td colspan="2">
	<a href="../images/PlansTemplate.xlsx" download="PlansTemplate.xlsx">Download Sample Template!</a>
	</td>
	</tr>
	</table><br>
	<br></TD>
	</TR>


	</TABLE>
	</TR></TABLE></BODY></HTML>
	<%
}
else
{
	out.println("Session Invalid... Please login again. <br> You are redirected to login page.<br>Click <a href=index.jsp> here </a> to continue");
	out.println("<META http-equiv=\"refresh\" CONTENT=\"0; URL=" + response.encodeURL("index.jsp") + "\">");
}
%>
