
package acme.features.student.enrolment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Errors;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import acme.services.SpamService;

@Service
public class StudentEnrolmentPublishService extends AbstractService<Student, Enrolment> {

	// Constants -------------------------------------------------------------
	public static final String[]			PROPERTIES	= {
		"code", "motivation", "goals", "cardHolderName", "cardLowerNibble", "cardNumber", "expirationDate", "cvv"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentEnrolmentRepository	repository;
	@Autowired
	protected SpamService					spamService;


	// AbstractService <Student,Enrolment> ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int id;
		Enrolment enrolment;
		final Principal principal;
		Student student;

		id = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findEnrolmentById(id);
		principal = super.getRequest().getPrincipal();
		student = this.repository.findStudentByPrincipalId(principal.getActiveRoleId());
		status = student != null && enrolment.getStudent().equals(student) && enrolment.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment enrolment) {
		assert enrolment != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(enrolment, StudentEnrolmentPublishService.PROPERTIES);
		enrolment.setCourse(course);
	}

	@Override
	public void validate(final Enrolment enrolment) {
		assert enrolment != null;

		final String locale;
		final SimpleDateFormat dateFormat;
		final String dateString;
		Date expirationDate;
		final Errors errors;
		final String nullError = "student.enrolment.form.error.null";

		final boolean hasExpirationDateErrors;
		final String holderName = enrolment.getCardHolderName();
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.spamService.validateTextInput(enrolment.getCode()), "code", "enrolment.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("motivation"))
			super.state(this.spamService.validateTextInput(enrolment.getMotivation()), "motivation", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.spamService.validateTextInput(enrolment.getGoals()), "goals", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("cardLowerNibble"))
			super.state(this.spamService.validateTextInput(enrolment.getCardLowerNibble()), "cardLowerNibble", "enrolment.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("expirationDate"))
			super.state(this.spamService.validateTextInput(enrolment.getExpirationDate()), "expirationDate", "enrolment.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("cardHolderName"))

		{
			final boolean validCardHolderName = holderName != null && !holderName.isEmpty();

			super.state(validCardHolderName, "cardHolderName", nullError);
			super.state(this.spamService.validateTextInput(enrolment.getCardHolderName()), "cardHolderName", "enrolment.error.spam");

		}

		if (!super.getBuffer().getErrors().hasErrors("cardNumber")) {
			final boolean validCardNumber = enrolment.getCardNumber() != null;
			super.state(this.spamService.validateTextInput(enrolment.getCardNumber()), "cardNumber", "enrolment.error.spam");
			super.state(validCardNumber, "cardNumber", nullError);
		}

		if (!super.getBuffer().getErrors().hasErrors("cvv")) {
			final boolean validCVV = enrolment.getCvv() != null;
			super.state(this.spamService.validateTextInput(enrolment.getCvv()), "cvv", "enrolment.error.spam");

			super.state(validCVV, "cvv", nullError);
		}

		if (enrolment.getExpirationDate() != null) {
			locale = Locale.getDefault().getLanguage();
			dateFormat = new SimpleDateFormat(locale.equals("es") ? "MM/yy" : "yy/MM");
			dateFormat.setLenient(false);

			dateString = enrolment.getExpirationDate();
			try {
				expirationDate = dateFormat.parse(dateString);
			} catch (final ParseException e) {
				expirationDate = null;
			}

			errors = super.getBuffer().getErrors();
			hasExpirationDateErrors = errors.hasErrors("expirationDate");

			super.state(!hasExpirationDateErrors && expirationDate != null, "expirationDate", "student.enrolment.form.error.incorrectDateFormat");

			if (!hasExpirationDateErrors && expirationDate != null)
				super.state(MomentHelper.isFuture(expirationDate), "expirationDate", "student.enrolment.form.error.expirationDate");
		}
	}

	@Override
	public void perform(final Enrolment enrolment) {
		assert enrolment != null;

		enrolment.setDraftMode(false);
		enrolment.setCardLowerNibble(enrolment.getCardNumber().substring(12, 16));

		this.repository.save(enrolment);
	}

	@Override
	public void unbind(final Enrolment enrolment) {
		assert enrolment != null;

		List<Course> courses;
		final SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findManyCourses();
		choices = SelectChoices.from(courses, "code", enrolment.getCourse());

		tuple = super.unbind(enrolment, StudentEnrolmentPublishService.PROPERTIES);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("draftMode", enrolment.isDraftMode());

		super.getResponse().setData(tuple);
	}
}
