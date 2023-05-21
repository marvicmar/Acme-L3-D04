
package acme.features.administrator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configuration.Configuration;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorConfigurationUpdateService extends AbstractService<Administrator, Configuration> {

	// Constants -------------------------------------------------------------
	protected static final String[] PROPERTIES = {
			"defaultCurrency", "acceptedCurrencies", "spamWords", "spamThreshold"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorConfigurationRepository repository;


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
		Configuration configuration;

		configuration = this.repository.findConfiguration();

		super.getBuffer().setData(configuration);
	}

	@Override
	public void bind(final Configuration configuration) {
		assert configuration != null;

		super.bind(configuration, AdministratorConfigurationUpdateService.PROPERTIES);
	}

	@Override
	public void validate(final Configuration configuration) {
		assert configuration != null;
	}

	@Override
	public void perform(final Configuration configuration) {
		assert configuration != null;

		this.repository.save(configuration);
	}

	@Override
	public void unbind(final Configuration configuration) {
		assert configuration != null;

		Tuple tuple;

		tuple = super.unbind(configuration, AdministratorConfigurationUpdateService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
