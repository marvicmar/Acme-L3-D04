<%--
- list.jsp
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
<acme:list>
	<acme:list-column code="authenticated.auditingRecord.form.label.subject" path="subject" width="20%"/>	
	<acme:list-column code="authenticated.auditingRecord.form.label.assessment" path="assessment" width="20%"/>
	<acme:list-column code="authenticated.auditingRecord.form.label.startAudit" path="startAudit" width="20%"/>	
	<acme:list-column code="authenticated.auditingRecord.form.label.endAudit" path="endAudit" width="20%"/>
	<acme:list-column code="authenticated.auditingRecord.form.label.duration" path="duration" width="15%"/>	
	<acme:list-column code="authenticated.auditingRecord.form.label.mark" path="mark" width="5%"/>	
</acme:list>



<jstl:if test="${myAudit && auditDraftMode}">
	<acme:button code="authenticated.auditingRecord.form.button.create" action="/auditor/auditing-record/create?auditId=${auditId}"/>
</jstl:if>
<jstl:if test="${myAudit && !auditDraftMode}">
	<acme:button code="authenticated.auditingRecord.form.button.correction" action="/auditor/auditing-record/create?auditId=${auditId}"/>
</jstl:if>

