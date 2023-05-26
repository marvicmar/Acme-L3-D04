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
	<acme:input-textbox readonly="true" code="authenticated.audit.form.label.course" path="course.code"/>
	<acme:input-textbox readonly="true" code="authenticated.audit.form.label.firm" path="auditor.firm"/>
	<acme:input-textbox readonly="true" code="authenticated.audit.form.label.code" path="code"/>
	<acme:input-textarea readonly="true" code="authenticated.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textarea readonly="true" code="authenticated.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textarea readonly="true" code="authenticated.audit.form.label.weakPoints" path="weakPoints"/>


	<acme:button code="authenticated.audit.form.button.records" action="/authenticated/auditing-record/list?auditId=${id}"/>
	
	
</acme:form>
