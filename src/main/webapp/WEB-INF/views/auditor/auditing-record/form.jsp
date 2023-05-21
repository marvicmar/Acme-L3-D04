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
	<acme:input-textbox readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.subject" path="subject"/>
	<acme:input-textbox readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.assessment" path="assessment"/>
	<acme:input-moment readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.startAudit" path="startAudit"/>
	<acme:input-moment readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.endAudit" path="endAudit"/>
	<acme:input-textbox readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.mark" path="mark" placeholder="authenticated.auditingRecord.form.label.placeHolder.mark"/>
	<acme:input-url readonly="${special && auditDraftMode || !myAudit}" code="authenticated.auditingRecord.form.label.link" path="link"/>
	
	<jstl:if test="${myAudit}">
		<acme:input-checkbox readonly="true" code="authenticated.auditingRecord.form.label.draftMode" path="auditDraftMode"/>
	</jstl:if>
	<jstl:if test="${myAudit}">
		<acme:input-checkbox readonly="${!special}" code="authenticated.auditingRecord.form.label.special" path="special"/>
	</jstl:if>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && auditDraftMode && !special}">
			<acme:submit test="${myAudit && auditDraftMode}" code="authenticated.auditingRecord.form.button.update" action="/auditor/auditing-record/update"/>
			<acme:submit test="${myAudit && auditDraftMode}" code="authenticated.auditingRecord.form.button.delete" action="/auditor/auditing-record/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create' && special}">
			<acme:submit test="${myAudit}" code="authenticated.auditingRecord.form.button.correction" action="/auditor/auditing-record/create?auditId=${auditId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.auditingRecord.form.button.create" action="/auditor/auditing-record/create?auditId=${auditId}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
