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

package acme.features.any.peep;

import acme.entities.peep.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.BinderHelper;
import acme.framework.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnyPeepShowService extends AbstractService<Any, Peep> {

	//Constants

	protected static final String[] PROPERTIES = {
			"moment", "title", "nick", "message", "email", "link", "draftMode"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyPeepRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;
		int id;
		final Peep peep;

		id = super.getRequest().getData("id", int.class);
		peep = this.repository.findOnePeepById(id);
		status = peep != null;

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
		int peepId;
		Peep object;

		peepId = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePeepById(peepId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Peep object) {
		assert object != null;

		super.bind(object, AnyPeepShowService.PROPERTIES);
	}

	@Override
	public void validate(final Peep object) {
		assert object != null;
	}

	@Override
	public void perform(final Peep object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Peep object) {
		assert object != null;

		Tuple tuple;

		tuple = BinderHelper.unbind(object, AnyPeepShowService.PROPERTIES);
		super.getResponse().setData(tuple);
	}
}
