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

import java.time.Duration;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditingRecord;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;

@Service
public class AuditingRecordListService extends AbstractService<Authenticated, AuditingRecord> {

	//Constants

	public final static String[]					PROPERTIES	= {
		"subject", "assessment", "start", "end", "mark", "link", "special"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditingRecordRepository	repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		Audit object;
		int auditRecordId;
		boolean rol;
		rol = super.getRequest().getPrincipal().hasRole(Authenticated.class);
		auditRecordId = super.getRequest().getData("auditId", int.class);

		object = this.repository.findOneAuditByAuditId(auditRecordId);

		super.getResponse().setAuthorised(rol && object != null && !object.isDraftMode());
	}

	@Override
	public void check() {
		Boolean status;
		status = super.getRequest().hasData("auditId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void load() {
		Collection<AuditingRecord> object;
		int auditId;

		auditId = super.getRequest().getData("auditId", int.class);

		object = this.repository.findAuditingRecordsByAuditId(auditId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		super.bind(object, AuditingRecordListService.PROPERTIES);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditingRecord object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		Tuple tuple;
		Duration duration;

		duration = object.getDuration();

		tuple = BinderHelper.unbind(object, AuditingRecordListService.PROPERTIES);
		tuple.put("duration", String.format("%d H %d m", duration.toHours(), duration.toMinutes() % 60));

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<AuditingRecord> objects) {
		assert objects != null;
		final int auditId;
		Audit audit;
		int userId;
		int auditUserId;

		auditId = super.getRequest().getData("auditId", int.class);
		audit = this.repository.findOneAuditByAuditId(auditId);
		userId = super.getRequest().getPrincipal().getAccountId();
		auditUserId = audit.getAuditor().getUserAccount().getId();
		super.getResponse().setGlobal("auditDraftMode", audit.isDraftMode());
		super.getResponse().setGlobal("auditId", auditId);
		super.getResponse().setGlobal("myAudit", userId == auditUserId);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
