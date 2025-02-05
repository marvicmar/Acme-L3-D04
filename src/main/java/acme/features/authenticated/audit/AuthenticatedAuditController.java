/*
 * AuthenticatedConsumerController.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.audit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit.Audit;
import acme.framework.components.accounts.Authenticated;
import acme.framework.controllers.AbstractController;

@Controller
public class AuthenticatedAuditController extends AbstractController<Authenticated, Audit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuthenticatedAuditShowService			showAuditService;
	@Autowired
	protected AuthenticatedAuditListCourseService	listAuditByCourseService;
	@Autowired
	protected AuthenticatedAuditListPublishService	listPublishAuditService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showAuditService);

		super.addCustomCommand("list-publish", "list", this.listPublishAuditService);
		super.addCustomCommand("list-course", "list", this.listAuditByCourseService);

	}

}
