
package acme.features.assistant.session;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enums.Approach;
import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import acme.services.SpamService;

@Service
public class AssistantSessionUpdateService extends AbstractService<Assistant, Session> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"title", "summary", "type", "start", "end", "link", "draftMode"
	};
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionRepository	repository;
	@Autowired
	protected SpamService					spamService;

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
		final int sessionId;
		final Session session;
		final Assistant assistant;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		assistant = session == null ? null : session.getTutorial().getAssistant();
		status = session != null && session.isDraftMode() && principal.hasRole(assistant);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Session session;
		int sessionId;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);

		super.getBuffer().setData(session);
	}

	@Override
	public void bind(final Session object) {
		assert object != null;

		super.bind(object, AssistantSessionUpdateService.PROPERTIES);

	}

	@Override
	public void validate(final Session session) {
		assert session != null;
		Date OneDayAhead;
		boolean condition1;
		boolean condition2;
		if (!super.getBuffer().getErrors().hasErrors("start")) {
			OneDayAhead = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(session.getStart(), OneDayAhead), "start", "assistant.session.start-error");
		}
		if (!super.getBuffer().getErrors().hasErrors("end"))
			super.state(MomentHelper.isAfter(session.getEnd(), session.getStart()), "end", "assistant.session.finish-error");
		if (!super.getBuffer().getErrors().hasErrors("end")) {
			condition1 = MomentHelper.isLongEnough(session.getStart(), session.getEnd(), 1, ChronoUnit.HOURS);
			condition2 = MomentHelper.computeDuration(session.getStart(), session.getEnd()).getSeconds() <= Duration.ofHours(5).getSeconds();
			super.state(condition1 && condition2, "end", "assistant.session.error-end-time");
		}
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamService.validateTextInput(session.getTitle()), "title", "session.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(this.spamService.validateTextInput(session.getSummary()), "summary", "session.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamService.validateTextInput(session.getLink()), "link", "session.error.spam");

	}

	@Override
	public void perform(final Session session) {
		assert session != null;

		this.repository.save(session);
	}

	@Override
	public void unbind(final Session session) {
		assert session != null;

		Tutorial tutorial;
		SelectChoices choices;
		Tuple tuple;

		tutorial = session.getTutorial();
		choices = SelectChoices.from(Approach.class, session.getType());

		tuple = super.unbind(session, AssistantSessionShowService.PROPERTIES);
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("type", choices);
		tuple.put("tutorial", tutorial);
		tuple.put("tutorialDraftMode", tutorial.isDraftMode());

		super.getResponse().setData(tuple);
	}
}
