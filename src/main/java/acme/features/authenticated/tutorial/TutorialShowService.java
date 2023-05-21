
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class TutorialShowService extends AbstractService<Authenticated, Tutorial> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected TutorialRepository repository;


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
		int tutorialId;
		final Tutorial tutorial;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		tutorialId = super.getRequest().getData("id", int.class);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);
		tutorial = this.repository.findOneTutorialById(tutorialId);
		status = !tutorial.isDraftMode() && principal.isAuthenticated();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int tutorialId;
		Tutorial tutorial;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);

		super.getBuffer().setData(tutorial);
	}

	@Override
	public void unbind(final Tutorial tutorial) {
		assert tutorial != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", tutorial.getCourse());
		tuple = super.unbind(tutorial, "code", "title", "summary", "goals", "estimatedTime", "draftMode");
		tuple.put("course", choices);
		tuple.put("courses", courses);
		tuple.put("nameAssistant", tutorial.getAssistant().getSupervisor());

		super.getResponse().setData(tuple);
	}
}
