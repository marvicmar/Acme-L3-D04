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
	<acme:list-column code="authenticated.audit.form.label.course" path="course.code" width="20%"/>	
	<acme:list-column code="authenticated.audit.form.label.code" path="code" width="20%"/>
	<acme:list-column code="authenticated.audit.form.label.conclusion" path="conclusion" width="20%"/>	
	<acme:list-column code="authenticated.audit.form.label.strongPoints" path="strongPoints" width="20%"/>
	<acme:list-column code="authenticated.audit.form.label.weakPoints" path="weakPoints" width="20%"/>	

</acme:list>
<jstl:if test="${isAuditor }">
	<acme:button code="authenticated.audit.form.button.create" action="/auditor/audit/create"/>
	
</jstl:if>

