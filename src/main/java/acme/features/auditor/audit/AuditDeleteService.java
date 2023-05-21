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
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditDeleteService extends AbstractService<Auditor, Audit> {

	//Constants

	public final static String[]	PROPERTIES	= {
		"course.code", "code", "conclusion", "strongPoints", "weakPoints", "auditor.firm"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditRepository		repository;


	@Override
	public void authorise() {
		Boolean status;
		final Boolean isMine;
		int auditUserId;
		final int accountId = super.getRequest().getPrincipal().getAccountId();
		final int auditId = super.getRequest().getData("id", int.class);
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		auditUserId = this.repository.findOneAuditById(auditId).getAuditor().getUserAccount().getId();
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
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		super.bind(object, AuditDeleteService.PROPERTIES);
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Audit object) {
		Tuple tuple;

		tuple = super.unbind(object, AuditDeleteService.PROPERTIES);
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
