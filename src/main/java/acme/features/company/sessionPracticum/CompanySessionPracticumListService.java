
package acme.features.company.sessionPracticum;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.sessionPracticum.SessionPracticum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MessageHelper;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionPracticumListService extends AbstractService<Company, SessionPracticum> {

	// Constants --------------------------------------------------------------
	protected static final String[]				PROPERTIES	= {
		"title", "abstractSession", "description", "start", "end", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanySessionPracticumRepository	repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;
		Principal principal;
		Company company;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		company = practicum == null ? null : practicum.getCompany();
		status = practicum != null && principal.hasRole(company);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<SessionPracticum> sessionPracticums;
		int practicumId;

		practicumId = super.getRequest().getData("masterId", int.class);
		sessionPracticums = this.repository.findManySessionPracticumsByPracticumId(practicumId);

		super.getBuffer().setData(sessionPracticums);
	}

	@Override
	public void unbind(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		Tuple tuple;
		final String additional;
		String payload;
		Date start;
		Date end;

		start = sessionPracticum.getStart();
		end = sessionPracticum.getEnd();
		tuple = super.unbind(sessionPracticum, CompanySessionPracticumListService.PROPERTIES);
		additional = MessageHelper.getMessage(sessionPracticum.isAdditional() ? "company.session-practicum.list.label.yes" : "company.session-practicum.list.label.no");
		payload = String.format("%s; %s", sessionPracticum.getAbstractSession(), sessionPracticum.getDescription());
		tuple.put("payload", payload);
		tuple.put("additional", additional);
		tuple.put("exactDuration", MomentHelper.computeDuration(start, end).toHours());

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<SessionPracticum> sessionPracticums) {
		assert sessionPracticums != null;

		int practicumId;
		Practicum practicum;
		boolean showCreate;
		Principal principal;
		boolean extraAvailable;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		showCreate = practicum.isDraftMode() && principal.hasRole(practicum.getCompany());
		extraAvailable = sessionPracticums.stream().noneMatch(SessionPracticum::isAdditional);

		super.getResponse().setGlobal("masterId", practicumId);
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setGlobal("extraAvailable", extraAvailable);
	}
}
