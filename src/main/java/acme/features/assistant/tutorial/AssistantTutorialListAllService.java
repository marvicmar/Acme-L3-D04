
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialListAllService extends AbstractService<Assistant, Tutorial> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"code", "title", "summary", "goals", "estimatedTime", "draftMode"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialRepository	repository;

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
		Collection<Tutorial> tutorials;

		tutorials = this.repository.findManyTutorial();

		super.getBuffer().setData(tutorials);
	}

	@Override
	public void unbind(final Tutorial tutorial) {
		assert tutorial != null;

		Tuple tuple;

		tuple = super.unbind(tutorial, AssistantTutorialListAllService.PROPERTIES);

		super.getResponse().setData(tuple);
	}
}
