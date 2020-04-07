<%@page import="org.apache.commons.io.FilenameUtils"%>
<%@page import="com.utility.servlet.MultipartRequestParameter"%>
<%@page import="org.apache.commons.fileupload.*"%>
<%@page import="com.youbb.api.exceptions.YOUBBAPIException"%>
<%@page import="com.you.portal.youplans.*"%>
<%@page import="org.apache.commons.collections4.map.LinkedMap"%>
<%@page import="java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
String errorMsg=null;

if(session.getAttribute("login") != null && !"".equals(session.getAttribute("login"))) {
	String uid = (String)session.getAttribute("login");
	Vector rights=(Vector) session.getAttribute("rights");
	String ct=(String)session.getAttribute("ct");
		
		if(!rights.contains("172")) {
%>
			<jsp:forward page="../success_reject.jsp">	
	   			<jsp:param name="error" value="You have no rights to see this page" />	
			</jsp:forward>
			<%
			  }
				List<YouPlanT> 	youPlant = null;
			  	try {
			  	  ArrayList<FileItem> fileItemList = new ArrayList<FileItem>(0);
			  	  Properties requestProperties = MultipartRequestParameter.getParameters(request, fileItemList);
			  	  YouPlanServices youPlantService=new YouPlanServices();
			  	  String fileName = fileItemList.get(0).getName();
				  String extension = FilenameUtils.getExtension(fileName);
				  youPlant=YouPlanExcelRead.convertXLSXToListOfBean(fileItemList.get(0).getInputStream());
				  YouPlanServices.uploadPlanCreate(youPlant);
				} catch (YOUBBAPIException e) {
					e.printStackTrace();
				  	errorMsg = e.getMessage();
			  	} catch (Exception e) {
			  		e.printStackTrace();
				  	errorMsg = e.getMessage();
			  	}
			}
			

			%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML><HEAD><TITLE>YOU Broadband India Ltd.</TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<META content="MSHTML 6.00.2600.0" name=GENERATOR>
<LINK href="../styles/style.css" type=text/css rel=stylesheet>
<style>
.alert {
	  padding: 20px;
	  background-color: #f44336;
	  color: white;
	  opacity: 1;
	  transition: opacity 0.6s;
	  margin-bottom: 15px;
	}

	.alert.success {background-color: #4CAF50;}
	.alert.info {background-color: #2196F3;}
	.alert.warning {background-color: #ff9800;}

	.closebtn {
	  margin-left: 15px;
	  color: white;
	  font-weight: bold;
	  float: right;
	  font-size: 22px;
	  line-height: 20px;
	  cursor: pointer;
	  transition: 0.3s;
	}

	.closebtn:hover {
	  color: black;
	}
</style>
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
                        background=images/strip_menu.jpg border=0>
                    
                          <TR>
                            <TD vAlign=center align=left>&nbsp;</TD>
                            <TD vAlign=center align=right height=23><a href=../plan_creation/uploadplan_index.jsp>Menu</a>&nbsp;&nbsp;&nbsp;
				<a href=../logout.jsp>Logout</a>                            
                            </TD></TR></TABLE> <h5><div align="right"><a href="../plan_creation/uploadplan_index.jsp">Upload Again</a></div></h5></TD></TR></TABLE></TD></TR></TABLE></TD></TR></TABLE></TD></TR></TABLE>
<div align="center"><BR>
  <B>Auto Plan creation</B>
  <br><br><br>
  <br>
  	
<td colspan="8" bgcolor="#bad9eb" height="21" text-align="center" font-weight="bold" margin-left="50px">
<%
 if (errorMsg!=null && !errorMsg.equals("")) {
%>
<div class="alert">
	<span class="closebtn">&times;</span> 
	<strong>Error.!</strong><%=errorMsg%>
</div>
<%
 } else {
%>
	<div class="alert success">
	<span class="closebtn">&times;</span> 
	<strong>Uploaded Successfully.!</strong>Plans has been created sucessfully to staging Portal.Please process further activity.</strong>
	</div>
<%
	}
%>
</td>
  <br>
</div>
<br />
</body>
</html>