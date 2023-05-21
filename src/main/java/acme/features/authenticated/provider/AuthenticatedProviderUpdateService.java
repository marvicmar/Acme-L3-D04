/*
 * AuthenticatedProviderUpdateService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Provider;
import acme.services.SpamService;

@Service
public class AuthenticatedProviderUpdateService extends AbstractService<Authenticated, Provider> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedProviderRepository	repository;
	@Autowired
	protected SpamService						spamService;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Provider object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneProviderByUserAccountId(userAccountId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Provider object) {
		assert object != null;

		super.bind(object, "company", "sector");
	}

	@Override
	public void validate(final Provider object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("company"))
			super.state(this.spamService.validateTextInput(object.getCompany()), "company", "provider.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("sector"))
			super.state(this.spamService.validateTextInput(object.getSector()), "sector", "provider.error.spam");
	}

	@Override
	public void perform(final Provider object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Provider object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "company", "sector");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
