
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
public class AssistantSessionCreateService extends AbstractService<Assistant, Session> {

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
		Principal principal;
		Tutorial tutorial;
		int tutorialid;

		tutorialid = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialid);
		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Session session;
		Tutorial tutorial;
		int tutorialid;

		tutorialid = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialid);
		session = new Session();
		session.setDraftMode(true);
		session.setTutorial(tutorial);

		super.getBuffer().setData(session);
	}

	@Override
	public void bind(final Session session) {
		assert session != null;
		super.bind(session, AssistantSessionCreateService.PROPERTIES);
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

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(Approach.class, session.getType());

		tuple = super.unbind(session, AssistantSessionCreateService.PROPERTIES);
		tuple.put("id", super.getRequest().getData("id", int.class));
		tuple.put("type", choices);
		tuple.put("tutorialDraftMode", session.getTutorial().isDraftMode());
		super.getResponse().setData(tuple);
	}

}
