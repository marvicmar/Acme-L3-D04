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
    <acme:input-textbox code="authenticated.company.form.label.name" path="name"/>
    <acme:input-textbox code="authenticated.company.form.label.vatNumber" path="vatNumber"/>
    <acme:input-textarea code="authenticated.company.form.label.summary" path="summary"/>
    <acme:input-url code="authenticated.company.form.label.link" path="link"/>
    
    <acme:submit test="${_command == 'create'}" code="authenticated.company.form.button.create" action="/authenticated/company/create"/>
    <acme:submit test="${_command == 'update'}" code="authenticated.company.form.button.update" action="/authenticated/company/update"/>
</acme:form>
