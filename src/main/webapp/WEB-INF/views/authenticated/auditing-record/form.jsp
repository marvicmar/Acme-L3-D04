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
	<acme:input-textbox readonly="true" code="authenticated.auditingRecord.form.label.subject" path="subject"/>
	<acme:input-textbox readonly="true" code="authenticated.auditingRecord.form.label.assessment" path="assessment"/>
	<acme:input-moment readonly="true" code="authenticated.auditingRecord.form.label.startAudit" path="start"/>
	<acme:input-moment readonly="true" code="authenticated.auditingRecord.form.label.endAudit" path="end"/>
	<acme:input-textbox readonly="true" code="authenticated.auditingRecord.form.label.mark" path="mark"/>
	<acme:input-url readonly="true" code="authenticated.auditingRecord.form.label.link" path="link"/>
	

	<jstl:if test="${special}">
		<acme:input-checkbox readonly="true" code="authenticated.auditingRecord.form.label.special" path="special"/>
	</jstl:if>

</acme:form>
