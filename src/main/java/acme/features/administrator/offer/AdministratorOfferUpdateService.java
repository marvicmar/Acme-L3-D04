
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;

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
public class AdministratorOfferUpdateService extends AbstractService<Administrator, Offer> {

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
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int id;
		Offer offer;

		id = super.getRequest().getData("id", int.class);
		offer = this.repository.findOneOfferById(id);
		status = offer != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Offer object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneOfferById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;

		object.setInstantiation(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;

		Date start;
		Date end;

		start = object.getStartDate();
		end = object.getEndDate();

		if (!super.getBuffer().getErrors().hasErrors("startDate"))
			super.state(MomentHelper.isFuture(start), "startDate", "administrator.offer.error.code.start-future");
		if (!super.getBuffer().getErrors().hasErrors("endDate"))
			super.state(MomentHelper.isFuture(end), "endDate", "administrator.offer.error.code.end-future");
		if (!super.getBuffer().getErrors().hasErrors("startDate") && !super.getBuffer().getErrors().hasErrors("availabilityEnd"))
			super.state(MomentHelper.isBefore(start, end), "startDate", "administrator.offer.error.code.start-before-end");
		if (!super.getBuffer().getErrors().hasErrors("startDate") && !super.getBuffer().getErrors().hasErrors("availabilityEnd")) {
			super.state(MomentHelper.isLongEnough(start, end, 7, ChronoUnit.DAYS), "startDate", "administrator.offer.error.code.short-availability");
			super.state(MomentHelper.isLongEnough(object.getInstantiation(), start, 1, ChronoUnit.DAYS), "endDate", "administrator.offer.error.code.short-start");
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
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, AdministratorOfferCreateService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, AdministratorOfferCreateService.PROPERTIES);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
