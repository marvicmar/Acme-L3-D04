
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import acme.services.SpamService;

@Service
public class AssistantTutorialUpdateService extends AbstractService<Assistant, Tutorial> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"code", "title", "summary", "goals", "estimatedTime", "draftMode"
	};
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository	repository;
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
		final int tutorialId;
		final Tutorial tutorial;
		final Assistant assistant;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && tutorial.isDraftMode() && principal.hasRole(assistant);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Tutorial tutorial;
		int tutorialId;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);

		super.getBuffer().setData(tutorial);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, AssistantTutorialUpdateService.PROPERTIES);
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial tutorial;

			tutorial = this.repository.findOneTutorialByCode(object.getCode());
			super.state(tutorial == null || tutorial.getId() == object.getId(), "code", "assistant.tutorial.form.error.not-unique-code");
			super.state(this.spamService.validateTextInput(object.getCode()), "code", "tutorial.error.spam");

		}
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamService.validateTextInput(object.getTitle()), "title", "tutorial.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(this.spamService.validateTextInput(object.getSummary()), "summary", "tutorial.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.spamService.validateTextInput(object.getGoals()), "goals", "tutorial.error.spam");
	}

	@Override
	public void perform(final Tutorial tutorial) {
		assert tutorial != null;

		this.repository.save(tutorial);
	}

	@Override
	public void unbind(final Tutorial tutorial) {
		assert tutorial != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", tutorial.getCourse());

		tuple = super.unbind(tutorial, AssistantTutorialUpdateService.PROPERTIES);
		tuple.put("course", choices);
		tuple.put("courses", courses);

		super.getResponse().setData(tuple);
	}
}
