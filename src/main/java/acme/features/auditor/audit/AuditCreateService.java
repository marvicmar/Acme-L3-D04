/*
 * AuthenticatedConsumerCreateService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.courses.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import acme.services.SpamService;

@Service
public class AuditCreateService extends AbstractService<Auditor, Audit> {

	//Constants

	public final static String[]	PROPERTIES	= {
		"course.code", "code", "conclusion", "strongPoints", "weakPoints", "auditor.firm"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditRepository		repository;
	@Autowired
	protected SpamService			spamService;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Audit object;
		Principal principal;
		int userAccountId;
		Auditor auditor;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		auditor = this.repository.findOneAuditorByUserAccountId(userAccountId);

		object = new Audit();
		object.setAuditor(auditor);
		object.setCourse(new Course());
		object.setDraftMode(true);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		super.bind(object, AuditCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
		assert object.getCourse().getCode() != null;

		final Course course = this.repository.findOneCurseByCode(object.getCourse().getCode());
		object.setCourse(course);
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean existCourse;
			final boolean isUnique;

			existCourse = course == null;
			super.state(!existCourse, "course.code", "audit.error.not-exist-curse");

			isUnique = this.repository.isUniqueCodeAudit(object.getCode());
			super.state(isUnique, "code", "audit.error.exist-code");
			super.state(this.spamService.validateTextInput(object.getCode()), "code", "audit.error.spam");

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

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		Tuple tuple;

		tuple = super.unbind(object, AuditCreateService.PROPERTIES);
		tuple.put("course", object.getCourse());
		tuple.put("auditor", object.getAuditor());
		tuple.put("myAudit", true);
		tuple.put("draftMode", true);
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
