<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>


<acme:form>
	<acme:input-textbox readonly="true" code="authenticated.course.code" path="code"/>
	<acme:input-textbox readonly="true" code="authenticated.course.title" path="title"/>
	<acme:input-textbox readonly="true" code="authenticated.course.courseAbstract" path="courseAbstract"/>
	<acme:input-textbox readonly="true" code="authenticated.course.retailPrice" path="retailPrice"/>
	<acme:input-textbox readonly="true" code="authenticated.course.furtherInformation" path="furtherInformation"/>
	<acme:input-textbox readonly="true" code="authenticated.course.type" path="type"/>
	
	
	<acme:submit method="get" test="${!isAuditor}" code="authenticated.course.form.button.show-audit" action="/authenticated/audit/list-course?id=${id}"/>
	<acme:submit method="get" test="${isAuditor}" code="authenticated.course.form.button.show-audit" action="/auditor/audit/list-course?id=${id}"/>
</acme:form>
