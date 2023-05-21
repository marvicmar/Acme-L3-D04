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

package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit_record.AuditingRecord;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditingRecordDeleteService extends AbstractService<Auditor, AuditingRecord> {

	//Constants

	public final static String[]				PROPERTIES	= {
		"subject", "assessment", "startAudit", "mark", "endAudit", "link", "special"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordRepository	repository;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;
		int auditRecordId;
		AuditingRecord auditingRecord;
		Boolean isMine;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		auditRecordId = super.getRequest().getData("id", int.class);
		auditingRecord = this.repository.findOneAuditingRecordById(auditRecordId);
		isMine = userAccountId == auditingRecord.getAudit().getAuditor().getUserAccount().getId();

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
		AuditingRecord object;
		int auditId;

		auditId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditingRecordById(auditId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		super.bind(object, AuditorAuditingRecordDeleteService.PROPERTIES);
	}

	@Override
	public void validate(final AuditingRecord object) {
		if (!object.getAudit().isDraftMode())
			super.state(false, "draftMode", "audit.error.edit-draftMode");

	}

	@Override
	public void perform(final AuditingRecord object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		Tuple tuple;

		tuple = super.unbind(object, AuditorAuditingRecordDeleteService.PROPERTIES);
		tuple.put("draftMode", object.getAudit().isDraftMode());
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
