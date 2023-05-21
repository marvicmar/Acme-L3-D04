
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionListService extends AbstractService<Assistant, Session> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"title", "summary", "type", "start", "end", "link", "draftMode"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionRepository	repository;

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
		int tutorialId;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);
		principal = super.getRequest().getPrincipal();
		status = tutorial != null && principal.hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Session> sessions;
		int tutorialId;

		tutorialId = super.getRequest().getData("id", int.class);
		sessions = this.repository.findManySessionByTutorialId(tutorialId);

		super.getBuffer().setData(sessions);
	}

	@Override
	public void unbind(final Session session) {
		assert session != null;

		Tuple tuple;

		tuple = super.unbind(session, AssistantSessionListService.PROPERTIES);

		super.getResponse().setData(tuple);
	}
}
