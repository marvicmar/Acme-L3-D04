/*
 * AuthenticatedConsumerUpdateService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.courses.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.BinderHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditShowService extends AbstractService<Auditor, Audit> {

	//Constants

	public final static String[]	PROPERTIES	= {
		"id", "course.code", "code", "conclusion", "strongPoints", "weakPoints", "auditor.firm", "draftMode"
	};
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditRepository		repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;
		Audit object;
		int auditId;
		Auditor auditor;
		final boolean isMine;

		auditId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(auditId);
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		auditor = this.repository.findOneAuditorByUserAccountId(super.getRequest().getPrincipal().getAccountId());
		isMine = object.getAuditor().getId() == auditor.getId();

		super.getResponse().setAuthorised(status && (isMine || !object.isDraftMode()));

	}

	@Override
	public void check() {
		Boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void load() {
		Audit object;
		int auditId;
		auditId = super.getRequest().getData("id", int.class);

		object = this.repository.findOneAuditById(auditId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		super.bind(object, AuditShowService.PROPERTIES);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;
		Principal principal;
		int userAccountId;
		Collection<Course> courses;
		SelectChoices choices;

		Tuple tuple;
		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		tuple = BinderHelper.unbind(object, AuditShowService.PROPERTIES);
		tuple.put("myAudit", userAccountId == object.getAuditor().getUserAccount().getId());
		tuple.put("isAuditor", super.getRequest().getPrincipal().hasRole(Auditor.class));
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("auditDraftMode", object.isDraftMode());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}

}
