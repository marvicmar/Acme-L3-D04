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
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import acme.services.SpamService;

@Service
public class AuditPublishService extends AbstractService<Auditor, Audit> {

	//Constants

	protected final static String[]	PROPERTIES	= {
		"code", "conclusion", "strongPoints", "weakPoints", "auditor.firm"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditRepository		repository;
	@Autowired
	protected SpamService			spamService;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		Boolean status;
		final Boolean isMine;
		int auditUserId;
		final Audit audit;
		final int accountId = super.getRequest().getPrincipal().getAccountId();
		final int auditId = super.getRequest().getData("id", int.class);
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		audit = this.repository.findOneAuditById(auditId);
		auditUserId = audit.getAuditor().getUserAccount().getId();
		isMine = auditUserId == accountId;
		super.getResponse().setAuthorised(status && isMine);
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
		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, AuditCreateService.PROPERTIES);
		object.setCourse(course);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
		assert object.getCourse() != null;

		int sizeAudits;

		final Course course = this.repository.findOneCurseByCode(object.getCourse().getCode());
		object.setCourse(course);
		sizeAudits = this.repository.findSizeOfAudit(object.getId());
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean existCourse;

			existCourse = course == null;
			super.state(!existCourse, "course", "audit.error.not-exist-curse");

			super.state(this.spamService.validateTextInput(object.getCode()), "code", "audit.error.spam");
			super.state(sizeAudits != 0, "code", "audit.error.publish");

		}
		if (!super.getBuffer().getErrors().hasErrors("conclusion"))
			super.state(this.spamService.validateTextInput(object.getConclusion()), "conclusion", "audit.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("strongPoints"))
			super.state(this.spamService.validateTextInput(object.getStrongPoints()), "strongPoints", "audit.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("weakPoints"))
			super.state(this.spamService.validateTextInput(object.getWeakPoints()), "weakPoints", "audit.error.spam");
	}

	@Override
	public void perform(final Audit object) {
		assert object != null;
		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;
		Principal principal;
		int userAccountId;
		final Collection<Course> courses;
		final SelectChoices choices;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		Tuple tuple;
		final int idAuditor = object.getAuditor().getUserAccount().getId();
		tuple = BinderHelper.unbind(object, AuditPublishService.PROPERTIES);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("myAudit", userAccountId == idAuditor);
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("isAuditor", super.getRequest().getPrincipal().hasRole(Auditor.class));
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
