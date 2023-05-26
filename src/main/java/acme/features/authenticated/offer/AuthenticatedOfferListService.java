
package acme.features.authenticated.offer;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offer.Offer;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.services.CurrencyService;

@Service
public class AuthenticatedOfferListService extends AbstractService<Authenticated, Offer> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"instantiation", "heading", "summary", "startDate", "endDate", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuthenticatedOfferRepository	repository;
	@Autowired
	protected CurrencyService				currencyService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final Collection<Offer> objects;
		final Date currentMoment = MomentHelper.getCurrentMoment();

		objects = this.repository.findAllNotFinishedOffers(currentMoment);

		super.getBuffer().setData(objects);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, AuthenticatedOfferListService.PROPERTIES);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, AuthenticatedOfferListService.PROPERTIES);
		tuple.put("price", this.currencyService.changeIntoSystemCurrency(object.getPrice()));

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Offer> objects) {
		assert objects != null;

		super.unbind(objects);
	}
}
