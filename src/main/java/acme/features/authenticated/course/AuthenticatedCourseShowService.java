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

package acme.features.authenticated.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.features.auditor.AuditorRepository;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.services.CurrencyService;

@Service
public class AuthenticatedCourseShowService extends AbstractService<Authenticated, Course> {

	//Constants

	protected final static String[]			PROPERTIES	= {
		"id", "code", "title", "courseAbstract", "retailPrice", "link", "type"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedCourseRepository	repository;
	@Autowired
	protected CurrencyService				currencyService;

	@Autowired
	protected AuditorRepository				auditorRepository;

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
		Course object;
		int courseId;

		courseId = super.getRequest().getData("id", int.class);

		object = this.repository.findOneCourseById(courseId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, AuthenticatedCourseShowService.PROPERTIES);
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		int accountId;
		boolean isAuditor;
		Tuple tuple;
		accountId = super.getRequest().getPrincipal().getAccountId();
		isAuditor = this.auditorRepository.findOneAuditorByUserAccountId(accountId) != null;
		tuple = BinderHelper.unbind(object, AuthenticatedCourseShowService.PROPERTIES);
		tuple.put("retailPrice", this.currencyService.changeIntoSystemCurrency(object.getRetailPrice()));
		super.getResponse().setData(tuple);
		super.getResponse().setGlobal("isAuditor", isAuditor);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
