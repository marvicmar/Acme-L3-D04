
package acme.features.student.enrolment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import acme.services.SpamService;

@Service
public class StudentEnrolmentCreateService extends AbstractService<Student, Enrolment> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"code", "motivation", "goals", "cardHolderName"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentEnrolmentRepository	repository;
	@Autowired
	protected SpamService					spamService;


	// AbstractService <Student,Enrolment> ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Student.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		Principal principal;

		object = new Enrolment();

		principal = super.getRequest().getPrincipal();
		final int principalId = principal.getActiveRoleId();

		object.setStudent(this.repository.findStudentByPrincipalId(principalId));
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment enrolment) {
		assert enrolment != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(enrolment, StudentEnrolmentCreateService.PROPERTIES);
		enrolment.setCourse(course);
	}

	@Override
	public void validate(final Enrolment enrolment) {
		assert enrolment != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment existing;

			existing = this.repository.findOneEnrolmentByCode(enrolment.getCode());
			super.state(existing == null, "code", "asistant.tutorial.form.error.not-unique-code");
			super.state(this.spamService.validateTextInput(enrolment.getCode()), "code", "enrolment.error.spam");

		}
		if (!super.getBuffer().getErrors().hasErrors("motivation"))
			super.state(this.spamService.validateTextInput(enrolment.getMotivation()), "motivation", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.spamService.validateTextInput(enrolment.getGoals()), "goals", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("cardHolderName"))
			super.state(this.spamService.validateTextInput(enrolment.getCardHolderName()), "cardHolderName", "enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("cardLowerNibble"))
			super.state(this.spamService.validateTextInput(enrolment.getCardLowerNibble()), "cardLowerNibble", "enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("cardNumber"))
			super.state(this.spamService.validateTextInput(enrolment.getCardNumber()), "cardNumber", "enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("expirationDate"))
			super.state(this.spamService.validateTextInput(enrolment.getExpirationDate()), "expirationDate", "enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("cvv"))
			super.state(this.spamService.validateTextInput(enrolment.getCvv()), "cvv", "enrolment.error.spam");

	}

	@Override
	public void perform(final Enrolment enrolment) {
		assert enrolment != null;

		//enrolment.setDraftMode(false);

		this.repository.save(enrolment);
	}

	@Override
	public void unbind(final Enrolment enrolment) {
		assert enrolment != null;

		List<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findManyCourses();
		choices = SelectChoices.from(courses, "title", enrolment.getCourse());

		tuple = super.unbind(enrolment, StudentEnrolmentCreateService.PROPERTIES);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("draftMode", enrolment.isDraftMode());

		super.getResponse().setData(tuple);
	}

}
