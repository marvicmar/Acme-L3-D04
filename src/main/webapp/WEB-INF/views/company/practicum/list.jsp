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
  <acme:list-column code="company.practicum.list.label.code" path="code" width="10%"/>
  <acme:list-column code="company.practicum.list.label.estimatedTimeInHours" path="estimatedTimeInHours" width="10%"/>
  <acme:list-column code="company.practicum.list.label.title" path="title" width="75%"/>
  <jstl:if test="${_command == 'list-mine'}">
    <acme:list-column code="company.practicum.list.label.published" path="published" width="5%"/>
  </jstl:if>
  <acme:list-payload path="payload"/>
</acme:list>	
<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="company.practicum.list.button.create" action="/company/practicum/create"/>
</jstl:if>	
