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

package acme.features.authenticated.audit;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuthenticatedAuditListCourseService extends AbstractService<Authenticated, Audit> {

	//Constants

	protected final static String[]			PROPERTIES	= {
		"id", "course.code", "code", "conclusion", "strongPoints", "weakPoints", "draftMode"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditRepository	repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Authenticated.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void check() {
		Boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void load() {
		final List<Audit> object;
		int courseId;
		int userAccountId;
		userAccountId = super.getRequest().getPrincipal().getAccountId();

		courseId = super.getRequest().getData("id", int.class);

		object = this.repository.findAuditsByCourse(courseId).stream().filter(a -> !a.isDraftMode() || a.getAuditor().getUserAccount().getId() == userAccountId).collect(Collectors.toList());

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		super.bind(object, AuthenticatedAuditListCourseService.PROPERTIES);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
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

		tuple = BinderHelper.unbind(object, AuthenticatedAuditListCourseService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Audit> objects) {
		super.getResponse().setGlobal("isAuditor", super.getRequest().getPrincipal().hasRole(Auditor.class));
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
