
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.enums.Approach;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityDeleteService extends AbstractService<Student, Activity> {

	// Constants -------------------------------------------------------------
	public static final String[]		PROPERTIES	= {
		"title", "summary","type" ,"startDate", "endDate", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository	repository;

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
		int id;
		Activity activity;
		final Principal principal;
		Student student;

		id = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(id);
		principal = super.getRequest().getPrincipal();
		student = this.repository.findStudentByPrincipalId(principal.getActiveRoleId());
		status = student != null && activity.getEnrolment().getStudent().equals(student) && !activity.getEnrolment().isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Activity object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findActivityById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, StudentActivityDeleteService.PROPERTIES);
	}
	@Override
	public void validate(final Activity object) {
		assert object != null;
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(Approach.class, object.getType());

		tuple = super.unbind(object, StudentActivityDeleteService.PROPERTIES);
		tuple.put("types", choices);
		tuple.put("readonly", object.getEnrolment().isDraftMode());

		super.getResponse().setData(tuple);
	}

}
