
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
	<acme:input-textbox code="student.enrolment.form.label.code" path="code"/>
	<acme:input-textarea code="student.enrolment.form.label.motivation" path="motivation"/>
	<acme:input-textarea code="student.enrolment.form.label.goals" path="goals"/>
	<acme:input-select code="student.enrolment.form.label.course" path="course" choices="${courses}"/>
	<acme:input-textbox code="student.enrolment.form.label.workTime" path="workTime" readonly="true"/>
	<jstl:if test="${(_command != 'create')}">
		<acme:input-textbox code="student.enrolment.form.label.cardHolderName" path="cardHolderName"/>
		<acme:input-textbox code="student.enrolment.form.label.cardNumber" path="cardNumber"/>
		<acme:input-textbox code="student.enrolment.form.label.expirationDate" path="expirationDate"/>
		<acme:input-textbox code="student.enrolment.form.label.cvv" path="cvv"/>
	</jstl:if>

	<jstl:choose>	 
		<jstl:when test="${(_command == 'show'||_command == 'update'||_command == 'delete'||_command == 'publish') && draftMode == false}">
			<acme:button code="student.enrolment.form.button.activities" action="/student/activity/list?id=${id}"/>
		</jstl:when>	
		<jstl:when test="${(_command == 'show'||_command == 'update'||_command == 'delete'||_command == 'publish') && draftMode == true}">
			<acme:submit code="student.enrolment.form.button.update" action="/student/enrolment/update"/>
			<acme:submit code="student.enrolment.form.button.delete" action="/student/enrolment/delete"/>
			<acme:submit code="student.enrolment.form.button.publish" action="/student/enrolment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="student.enrolment.form.button.create" action="/student/enrolment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>