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

package acme.features.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import acme.services.SpamService;

@Service
public class AuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	//Constants

	protected static final String[]	PROPERTIES	= {
		"firm", "proffesionalId", "certifications", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorRepository		repository;
	@Autowired
	protected SpamService			spamService;

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
		Auditor object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneAuditorByUserAccountId(userAccountId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;

		super.bind(object, AuditorUpdateService.PROPERTIES);
	}

	@Override
	public void validate(final Auditor object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("firm"))
			super.state(this.spamService.validateTextInput(object.getFirm()), "firm", "auditor.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("proffesionalId"))
			super.state(this.spamService.validateTextInput(object.getProffesionalId()), "proffesionalId", "auditor.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("certifications"))
			super.state(this.spamService.validateTextInput(object.getCertifications()), "certifications", "auditor.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamService.validateTextInput(object.getLink()), "link", "auditor.error.spam");

	}

	@Override
	public void perform(final Auditor object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;

		Tuple tuple;

		tuple = BinderHelper.unbind(object, AuditorUpdateService.PROPERTIES);
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
