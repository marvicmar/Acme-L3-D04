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

package acme.features.authenticated.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecord.AuditingRecord;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordShowService extends AbstractService<Authenticated, AuditingRecord> {

	//Constants

	protected final static String[]					PROPERTIES	= {
		"id", "subject", "assessment", "start", "end", "mark", "link", "special"
	};
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditingRecordRepository	repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		AuditingRecord object;
		int auditingRecordId;
		boolean rol;
		rol = super.getRequest().getPrincipal().hasRole(Authenticated.class);
		auditingRecordId = super.getRequest().getData("id", int.class);

		object = this.repository.findOneAuditingRecordById(auditingRecordId);

		super.getResponse().setAuthorised(rol && object != null && !object.getAudit().isDraftMode());
	}

	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id");

		super.getResponse().setChecked(status);
	}

	@Override
	public void load() {
		AuditingRecord object;
		int auditingRecordId;

		auditingRecordId = super.getRequest().getData("id", int.class);

		object = this.repository.findOneAuditingRecordById(auditingRecordId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		super.bind(object, AuditingRecordShowService.PROPERTIES);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		final Auditor auditor;
		final int userAccountId;
		Tuple tuple;
		int userAuditorId;
		Boolean draftMode;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		auditor = object.getAudit().getAuditor();
		userAuditorId = auditor.getUserAccount().getId();
		draftMode = object.getAudit().isDraftMode();

		tuple = BinderHelper.unbind(object, AuditingRecordShowService.PROPERTIES);
		tuple.put("mark", object.getMark().toString());
		tuple.put("myAudit", userAccountId == userAuditorId);
		tuple.put("auditDraftMode", draftMode);
		tuple.put("audit", draftMode);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
