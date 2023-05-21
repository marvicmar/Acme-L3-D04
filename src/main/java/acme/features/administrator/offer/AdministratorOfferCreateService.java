
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import acme.framework.components.models.Errors;
import acme.services.SpamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"instantiation", "heading", "summary", "startDate", "endDate", "price", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository	repository;
	@Autowired
	protected SpamService spamDetector;

	// AbstractService interface ----------------------------------------------


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
		Offer object;

		object = new Offer();
		object.setInstantiation(MomentHelper.getCurrentMoment());

		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, AdministratorOfferCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;

		Date start;
		Date end;
		Errors errors;

		start = object.getStartDate();
		end = object.getEndDate();
		errors = super.getBuffer().getErrors();

		if (!errors.hasErrors("startDate"))
			super.state(MomentHelper.isFuture(start), "startDate", "administrator.offer.error.code.start-future");
		if (!errors.hasErrors("startDate"))
			super.state(MomentHelper.isFuture(end), "endDate", "administrator.offer.error.code.end-future");
		if (!errors.hasErrors("startDate") && !errors.hasErrors("endDate"))
			super.state(MomentHelper.isBefore(start, end), "startDate", "administrator.offer.error.code.start-before-end");
		if (!errors.hasErrors("startDate") && !errors.hasErrors("endDate")) {
			super.state(MomentHelper.isLongEnough(start, end, 7, ChronoUnit.DAYS), "startDate", "administrator.offer.error.code.short-availability");
			super.state(MomentHelper.isLongEnough(start, MomentHelper.getCurrentMoment(), 1, ChronoUnit.DAYS), "endDate", "administrator.offer.error.code.short-start");
		}
		if (!super.getBuffer().getErrors().hasErrors("price"))
			super.state(object.getPrice().getAmount() >= 0, "price", "administrator.offer.error.code.price-negative");

		// Spam validation
		if (!super.getBuffer().getErrors().hasErrors("heading"))
			super.state(this.spamDetector.validateTextInput(object.getHeading()), "heading", "administrator.offer.form.error.spam.heading");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(this.spamDetector.validateTextInput(object.getSummary()), "summary", "administrator.offer.form.error.spam.summary");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamDetector.validateTextInput(object.getLink()), "link", "administrator.offer.error.form.spam.link");
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;

		object.setInstantiation(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		Tuple tuple;

		tuple = super.unbind(object, AdministratorOfferCreateService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
