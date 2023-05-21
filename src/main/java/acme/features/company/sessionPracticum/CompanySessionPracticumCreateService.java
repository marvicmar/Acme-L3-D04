
package acme.features.company.sessionPracticum;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.sessionPracticum.SessionPracticum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;
import acme.services.SpamService;

@Service
public class CompanySessionPracticumCreateService extends AbstractService<Company, SessionPracticum> {

	// Constants -------------------------------------------------------------
	public static final int						ONE_WEEK			= 1;
	protected static final String[]				PROPERTIES_BIND		= {
		"title", "abstractSession", "description", "start", "end", "link"
	};
	protected static final String[]				PROPERTIES_UNBIND	= {
		"title", "abstractSession", "description", "start", "end", "link", "additional"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanySessionPracticumRepository	repository;
	@Autowired
	protected SpamService						spamDetector;

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
		boolean hasExtraAvailable;
		Principal principal;
		Company company;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		status = false;

		if (practicum != null) {
			hasExtraAvailable = this.repository.findManySessionPracticumsByExtraAvailableAndPracticumId(practicum.getId()).isEmpty();
			company = practicum.getCompany();

			status = (practicum.isDraftMode() || hasExtraAvailable) && principal.hasRole(company);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SessionPracticum sessionPracticum;
		int practicumId;
		Practicum practicum;
		boolean draftMode;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		draftMode = practicum.isDraftMode();

		sessionPracticum = new SessionPracticum();
		sessionPracticum.setPracticum(practicum);
		sessionPracticum.setAdditional(!draftMode);

		super.getBuffer().setData(sessionPracticum);
	}

	@Override
	public void bind(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		super.bind(sessionPracticum, CompanySessionPracticumCreateService.PROPERTIES_BIND);
	}

	@Override
	public void validate(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		if (!super.getBuffer().getErrors().hasErrors("start") || !super.getBuffer().getErrors().hasErrors("end")) {
			Date start;
			Date end;
			Date inAWeekFromNow;
			Date inAWeekFromStart;

			start = sessionPracticum.getStart();
			end = sessionPracticum.getEnd();
			inAWeekFromNow = MomentHelper.deltaFromCurrentMoment(CompanySessionPracticumCreateService.ONE_WEEK, ChronoUnit.WEEKS);
			inAWeekFromStart = MomentHelper.deltaFromMoment(start, CompanySessionPracticumCreateService.ONE_WEEK, ChronoUnit.WEEKS);

			if (!super.getBuffer().getErrors().hasErrors("start"))
				super.state(MomentHelper.isAfter(start, inAWeekFromNow), "start", "company.session-practicum.error.start-after-now");
			if (!super.getBuffer().getErrors().hasErrors("end"))
				super.state(MomentHelper.isAfter(end, inAWeekFromStart), "end", "company.session-practicum.error.end-after-start");
		}

		if (!super.getBuffer().getErrors().hasErrors("confirmed")) {
			Practicum practicum;
			boolean confirmed;

			confirmed = super.getRequest().getData("confirmed", boolean.class);
			practicum = sessionPracticum.getPracticum();

			super.state(confirmed || practicum.isDraftMode(), "confirmed", "company.session-practicum.error.confirmed");
		}

		// Spam validation
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamDetector.validateTextInput(sessionPracticum.getTitle()), "title", "company.session-practicum.error.spam.title");
		if (!super.getBuffer().getErrors().hasErrors("abstractSession"))
			super.state(this.spamDetector.validateTextInput(sessionPracticum.getAbstractSession()), "abstractSession", "company.session-practicum.error.spam.abstract-session");
		if (!super.getBuffer().getErrors().hasErrors("description"))
			super.state(this.spamDetector.validateTextInput(sessionPracticum.getDescription()), "description", "company.session-practicum.error.spam.description");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamDetector.validateTextInput(sessionPracticum.getLink()), "link", "company.session-practicum.error.spam.link");

	}

	@Override
	public void perform(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		this.repository.save(sessionPracticum);
	}

	@Override
	public void unbind(final SessionPracticum sessionPracticum) {
		assert sessionPracticum != null;

		Practicum practicum;
		Tuple tuple;

		practicum = sessionPracticum.getPracticum();
		tuple = super.unbind(sessionPracticum, CompanySessionPracticumCreateService.PROPERTIES_UNBIND);
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.isDraftMode());

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
