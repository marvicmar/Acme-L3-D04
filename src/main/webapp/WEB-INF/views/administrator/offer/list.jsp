<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>



<acme:list>
	<acme:list-column code="administrator.offer.list.label.heading" path="heading" width="25%"/>
	<acme:list-column code="administrator.offer.list.label.startDate" path="startDate" width="25%"/>
	<acme:list-column code="administrator.offer.list.label.endDate" path="endDate" width="25%"/>
	<acme:list-column code="administrator.offer.list.label.price" path="price" width="25%"/>
</acme:list>
<acme:button code="administrator.offer.form.button.create" action="/administrator/offer/create"/>


