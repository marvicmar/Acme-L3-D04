
package acme.features.company.practicum;

import java.util.Collection;

import acme.services.SpamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.practicum.Practicum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumCreateService extends AbstractService<Company, Practicum> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"code", "title", "abstractPracticum", "goals", "estimatedTimeInHours"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumRepository	repository;
	@Autowired
	protected SpamService spamDetector;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Practicum practicum;
		Company company;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		company = this.repository.findOneCompanyById(principal.getActiveRoleId());
		practicum = new Practicum();
		practicum.setDraftMode(true);
		practicum.setCompany(company);

		super.getBuffer().setData(practicum);
	}

	@Override
	public void bind(final Practicum practicum) {
		assert practicum != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(practicum, CompanyPracticumCreateService.PROPERTIES);
		practicum.setCourse(course);
	}

	@Override
	public void validate(final Practicum practicum) {
		assert practicum != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean isUnique;

			isUnique = this.repository.findManyPracticumByCode(practicum.getCode()).isEmpty();

			super.state(isUnique, "code", "company.practicum.form.error.not-unique-code");
		}

		// Spam validation
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.spamDetector.validateTextInput(practicum.getCode()), "code", "company.practicum.form.error.spam.code");
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamDetector.validateTextInput(practicum.getTitle()), "title", "company.practicum.form.error.spam.title");
		if (!super.getBuffer().getErrors().hasErrors("abstractPracticum"))
			super.state(this.spamDetector.validateTextInput(practicum.getAbstractPracticum()), "abstractPracticum", "company.practicum.form.error.spam.abstractPracticum");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.spamDetector.validateTextInput(practicum.getGoals()), "goals", "company.practicum.form.error.spam.goals");
	}

	@Override
	public void perform(final Practicum practicum) {
		assert practicum != null;

		this.repository.save(practicum);
	}

	@Override
	public void unbind(final Practicum practicum) {
		assert practicum != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", practicum.getCourse());

		tuple = super.unbind(practicum, CompanyPracticumCreateService.PROPERTIES);
		tuple.put("draftMode", practicum.isDraftMode());
		tuple.put("course", choices);
		tuple.put("courses", courses);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
