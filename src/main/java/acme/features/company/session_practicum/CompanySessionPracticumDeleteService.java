
package acme.features.company.session_practicum;

import acme.entities.practicum.Practicum;
import acme.entities.session_practicum.SessionPracticum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanySessionPracticumDeleteService extends AbstractService<Company, SessionPracticum> {

	// Constants --------------------------------------------------------------
	protected static final String[] PROPERTIES = {
			"title", "abstractSession", "description", "start", "end", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanySessionPracticumRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int sessionPracticumId;
		SessionPracticum sessionPracticum;
		Principal principal;
		Practicum practicum;
		Boolean isDraftMode;
		Company company;


		principal = super.getRequest().getPrincipal();
		sessionPracticumId = super.getRequest().getData("id", int.class);
		sessionPracticum = this.repository.findOneSessionPracticumById(sessionPracticumId);
		status = false;

		if (sessionPracticum != null) {
			practicum = sessionPracticum.getPracticum();

			isDraftMode = practicum.isDraftMode();
			company = practicum == null ? null : practicum.getCompany();

			status = isDraftMode && principal.hasRole(company);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SessionPracticum sessionPracticum;
		int sessionPracticumId;

		sessionPracticumId = super.getRequest().getData("id", int.class);
		sessionPracticum = this.repository.findOneSessionPracticumById(sessionPracticumId);

		super.getBuffer().setData(sessionPracticum);
	}

	@Override
	public void bind(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		super.bind(sessionPracticum, CompanySessionPracticumDeleteService.PROPERTIES);
	}

	@Override
	public void validate(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;
	}

	@Override
	public void perform(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		this.repository.delete(sessionPracticum);
	}

	@Override
	public void unbind(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		Practicum practicum;
		Tuple tuple;

		practicum = sessionPracticum.getPracticum();
		tuple = super.unbind(sessionPracticum, CompanySessionPracticumUpdateService.PROPERTIES_UNBIND);
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.isDraftMode());

		super.getResponse().setData(tuple);
	}
}
