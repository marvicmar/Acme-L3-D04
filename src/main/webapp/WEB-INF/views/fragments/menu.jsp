<%--
- menu.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="acme.framework.helpers.PrincipalHelper,acme.roles.Provider,acme.roles.Consumer"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
	<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.peeps" action="/any/peep/list"/>
	</acme:menu-option>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.sersansan2" action="https://atletismofaa.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.alesanfel" action="https://www.crunchyroll.com/es-es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.alepervaz" action="https://open.spotify.com/?"/>
      		<acme:menu-suboption code="master.menu.anonymous.favorite-link.marvicmar" action="https://zacatrus.es/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated">
			<acme:menu-suboption code="master.menu.authenticated.favourite-link" action="http://www.example.com/"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.Tutorial" action="/authenticated/tutorial/list"/>
			<acme:menu-suboption code="master.menu.authenticated.all-notes" action="/authenticated/note/list"/>
			<acme:menu-suboption code="master.menu.authenticated.all-bulletins" action="/authenticated/bulletin/list"/>
			<acme:menu-suboption code="master.menu.authenticated.all-practicums" action="/authenticated/practicum/list"/>
			<acme:menu-suboption code="master.menu.authenticated.offer" action="/authenticated/offer/list"/>
			<acme:menu-suboption code="master.menu.authenticated.all-audits" action="/authenticated/audit/list-publish"/>
			<acme:menu-suboption code="master.menu.auditor.list-by-course" action="/authenticated/course/list-course"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/populate-sample"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.dashboard" action="/administrator/administrator-dashboard/show"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.configuration" action="/administrator/configuration/show"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.create.bulletin" action="/administrator/bulletin/create"/>
			<acme:menu-suboption code="master.menu.administrator.banner" action="/administrator/banner/list"/>
			<acme:menu-suboption code="master.menu.administrator.offer" action="/administrator/offer/list"/>
		</acme:menu-option>


		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.assistant" access="hasRole('Assistant')">
			<acme:menu-suboption code="master.menu.assistant.list-all" action="/assistant/tutorial/list-all"/>
			<acme:menu-suboption code="master.menu.assistant.list-mine" action="/assistant/tutorial/list-mine"/>
			<acme:menu-suboption code="master.menu.assistant.session.list-mine" action="/assistant/session/list-mine"/> <!-- Creo que no es necesario -->
      		<acme:menu-separator/>
      		<acme:menu-suboption code="master.menu.assistant.dashboard" action="/assistant/assistant-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>


		<acme:menu-option code="master.menu.student" access="hasRole('Student')">
			<acme:menu-suboption code="master.menu.student.enrolment.list" action="/student/enrolment/list"/>
			<acme:menu-suboption code="master.menu.student.courses" action="/student/course/list"/>
			<acme:menu-suboption code="master.menu.student.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.list-mine" action="/auditor/audit/list-mine" />
			<acme:menu-suboption code="master.menu.auditor.list-publish" action="/auditor/audit/list-publish"/>
			<acme:menu-suboption code="master.menu.auditor.list-by-course" action="/authenticated/course/list-course"/>

		</acme:menu-option>

		<acme:menu-option code="master.menu.company" access="hasRole('Company')">
			<acme:menu-suboption code="master.menu.company.favourite-link" action="http://www.example.com/"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.company.all-practicums" action="/company/practicum/list-all"/>
			<acme:menu-suboption code="master.menu.company.my-practicums" action="/company/practicum/list-mine"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.company.dashboard" action="/company/company-dashboard/show"/>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-company" action="/authenticated/company/create" access="!hasRole('Company')"/>
			<acme:menu-suboption code="master.menu.user-account.company" action="/authenticated/company/update" access="hasRole('Company')"/>
			<acme:menu-suboption code="master.menu.user-account.become-student" action="/authenticated/student/create" access="!hasRole('Student')"/>
			<acme:menu-suboption code="master.menu.user-account.student" action="/authenticated/student/update" access="hasRole('Student')"/>
			<acme:menu-suboption code="master.menu.user-account.become-auditor" action="/authenticated/auditor/create" access="!hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.auditor" action="/authenticated/auditor/update" access="hasRole('Auditor')"/>
			<acme:menu-suboption code="master.menu.user-account.become-company" action="/authenticated/company/create" access="!hasRole('Company')"/>
			<acme:menu-suboption code="master.menu.user-account.company" action="/authenticated/company/update" access="hasRole('Company')"/>
			<acme:menu-suboption code="master.menu.user-account.become-assistant" action="/authenticated/assistant/create" access="!hasRole('Assistant')"/>
			<acme:menu-suboption code="master.menu.user-account.assistant" action="/authenticated/assistant/update" access="hasRole('Assistant')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

