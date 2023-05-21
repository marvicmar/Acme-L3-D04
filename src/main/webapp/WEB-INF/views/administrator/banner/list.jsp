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


<acme:list>
	<acme:list-column code="administrator.banner.form.label.instant" path="instant" width="20%"/>
	<acme:list-column code="administrator.banner.form.label.displayStart" path="displayStart" width="20%"/>
	<acme:list-column code="administrator.banner.form.label.displayEnd" path="displayEnd" width="20%"/>
	<acme:list-column code="administrator.banner.form.label.picture" path="picture" width="10%"/>
	<acme:list-column  code="administrator.banner.form.label.slogan" path="slogan" width="10%"/>
	<acme:list-column code="administrator.banner.form.label.link" path="link" width="10%"/>
</acme:list>

<acme:button code="administrator.banner.form.button.create" action="/administrator/banner/create"/>