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

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit_record.AuditingRecord;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import acme.services.SpamService;

@Service
public class AuditorAuditingRecordCreateService extends AbstractService<Auditor, AuditingRecord> {

	//Constants

	public final static String[]				PROPERTIES	= {
		"subject", "assessment", "startAudit", "mark", "endAudit", "link", "special"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordRepository	repository;
	@Autowired
	protected SpamService						spamService;


	@Override
	public void authorise() {
		boolean status;
		int userAccountId;
		int auditId;
		Audit audit;
		Boolean isMine;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		auditId = super.getRequest().getData("auditId", int.class);
		audit = this.repository.findOneAuditByAuditId(auditId);
		isMine = userAccountId == audit.getAuditor().getUserAccount().getId();

		super.getResponse().setAuthorised(status && isMine);
	}

	@Override
	public void check() {
		Boolean status;
		status = super.getRequest().hasData("auditId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void load() {
		AuditingRecord object;
		int auditId;
		Audit audit;

		auditId = super.getRequest().getData("auditId", int.class);
		audit = this.repository.findOneAuditByAuditId(auditId);

		object = new AuditingRecord();
		if (!audit.isDraftMode())
			object.setSpecial(!audit.isDraftMode());
		object.setAudit(audit);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		super.bind(object, AuditorAuditingRecordCreateService.PROPERTIES);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;

		final boolean draft = object.getAudit().isDraftMode();
		if (!super.getBuffer().getErrors().hasErrors("special"))
			super.state(draft || !draft && object.isSpecial(), "special", "audit.error.edit-draftMode");
		final Date start = object.getStartAudit();
		final Date end = object.getEndAudit();
		final Duration duration = MomentHelper.computeDuration(start, end);
		if (!super.getBuffer().getErrors().hasErrors("mark"))
			super.state(MomentHelper.isBefore(start, end), "mark", "auditingRecord.error.not-valid-mark");
		if (!super.getBuffer().getErrors().hasErrors("endAudit"))
			super.state(duration.toMinutes() >= 30, "endAudit", "auditingRecord.error.not-enougth-time");

		if (!super.getBuffer().getErrors().hasErrors("subject"))
			super.state(this.spamService.validateTextInput(object.getSubject()), "subject", "auditingRecord.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("assessment"))
			super.state(this.spamService.validateTextInput(object.getAssessment()), "assessment", "auditingRecord.error.spam");

	}

	@Override
	public void perform(final AuditingRecord object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		Tuple tuple;
		int auditId;

		auditId = object.getAudit().getId();

		tuple = super.unbind(object, AuditorAuditingRecordCreateService.PROPERTIES);
		tuple.put("myAudit", true);

		tuple.put("auditDraftMode", object.getAudit().isDraftMode());
		super.getResponse().setGlobal("auditId", auditId);
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
