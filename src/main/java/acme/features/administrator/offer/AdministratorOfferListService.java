
package acme.features.administrator.offer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.services.CurrencyService;

@Service
public class AdministratorOfferListService extends AbstractService<Administrator, Offer> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"instantiation", "heading", "summary", "startDate", "endDate", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorOfferRepository	repository;
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

		objects = this.repository.findManyOffer();

		super.getBuffer().setData(objects);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, AdministratorOfferListService.PROPERTIES);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, AdministratorOfferListService.PROPERTIES);
		tuple.put("price", this.currencyService.changeIntoSystemCurrency(object.getPrice()));

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Offer> objects) {
		assert objects != null;

		super.unbind(objects);
	}
}
