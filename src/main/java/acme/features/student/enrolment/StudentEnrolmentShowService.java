
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

@Service
public class StudentEnrolmentShowService extends AbstractService<Student, Enrolment> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"code", "motivation", "goals", "draftMode"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentEnrolmentRepository	repository;


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
		status = student != null && enrolment.getStudent().equals(student);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment enrolment;
		int id;

		id = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(enrolment);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Double workTime;
		List<Course> courses;
		SelectChoices choices;

		Tuple tuple;
		courses = this.repository.findManyCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		workTime = this.repository.finWorkTimeByEnrolmentId(object.getId());
		workTime = workTime != null ? workTime : 0.0;

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode");
		tuple.put("readonly", !object.isDraftMode());
		tuple.put("workTime", workTime);
		tuple.put("courseTitle", object.getCourse().getTitle());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
