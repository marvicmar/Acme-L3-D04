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

package acme.features.auditor.audit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit.Audit;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditController extends AbstractController<Auditor, Audit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditShowService			showAuditService;
	@Autowired
	protected AuditListMineService		listMineAuditService;
	@Autowired
	protected AuditListCourseService	listAuditByCourseService;
	@Autowired
	protected AuditUpdateService		updateAuditService;
	@Autowired
	protected AuditCreateService		createAuditService;
	@Autowired
	protected AuditDeleteService		deleteAuditService;
	@Autowired
	protected AuditPublishService		publishAuditService;
	@Autowired
	protected AuditListPublishService	listPublishAuditService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showAuditService);
		super.addBasicCommand("update", this.updateAuditService);
		super.addBasicCommand("create", this.createAuditService);
		super.addBasicCommand("delete", this.deleteAuditService);

		super.addCustomCommand("list-publish", "list", this.listPublishAuditService);
		super.addCustomCommand("list-mine", "list", this.listMineAuditService);
		super.addCustomCommand("list-course", "list", this.listAuditByCourseService);
		super.addCustomCommand("publish", "update", this.publishAuditService);
	}
}
