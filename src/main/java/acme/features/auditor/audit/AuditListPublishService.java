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
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditListPublishService extends AbstractService<Auditor, Audit> {

	//Constants

	public final static String[]	PROPERTIES	= {
		"id", "course.code", "code", "conclusion", "strongPoints", "weakPoints", "draftMode"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditRepository		repository;

	// AbstractService interface ----------------------------------------------ç


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
		final Collection<Audit> object;
		object = this.repository.findAuditsPublish();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		super.bind(object, AuditListPublishService.PROPERTIES);
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
		}
	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;

		tuple = BinderHelper.unbind(object, AuditListPublishService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Audit> objects) {
		super.getResponse().setGlobal("isAuditor", super.getRequest().getPrincipal().hasRole(Auditor.class));
		super.unbind(objects);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
