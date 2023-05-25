
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MessageHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionListMineService extends AbstractService<Assistant, Session> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"title", "summary", "type", "start", "end", "link", "draftMode", "tutorial"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionRepository	repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Session> sessions;
		final Principal principal;
		principal = super.getRequest().getPrincipal();
		sessions = this.repository.findManySessionByAssistantId(principal.getActiveRoleId());

		super.getBuffer().setData(sessions);
	}

	@Override
	public void unbind(final Session session) {
		assert session != null;
		final String draftMode;
		final Tutorial tutorial;

		Tuple tuple;
		tutorial = this.repository.findOneSessionById(session.getId()).getTutorial();
		draftMode = MessageHelper.getMessage(session.isDraftMode() ? "assistant.session.list.label.no" : "assistant.session.list.label.yes");
		tuple = super.unbind(session, AssistantSessionListMineService.PROPERTIES);
		tuple.put("draftMode", draftMode);
		tuple.put("tutorial", tutorial.getCode());
		super.getResponse().setData(tuple);
	}

}
