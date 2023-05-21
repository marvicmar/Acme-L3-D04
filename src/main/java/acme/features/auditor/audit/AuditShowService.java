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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
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

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

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

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		Tuple tuple;
		tuple = BinderHelper.unbind(object, AuditShowService.PROPERTIES);
		tuple.put("myAudit", userAccountId == object.getAuditor().getUserAccount().getId());
		tuple.put("isAuditor", super.getRequest().getPrincipal().hasRole(Auditor.class));
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
