
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.enrolment.Enrolment;
import acme.entities.enums.Approach;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import acme.services.SpamService;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

	// Constants -------------------------------------------------------------
	public static final String[]		PROPERTIES	= {
		"title", "summary", "startDate", "endDate", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository	repository;
	@Autowired
	protected SpamService				spamService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("enrolmentId", int.class);
		super.getResponse().setChecked(status);
	}
	@Override
	public void authorise() {
		boolean status;
		int enrolmentId;
		Enrolment enrolment;
		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		status = enrolment != null && super.getRequest().getPrincipal().hasRole(enrolment.getStudent());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;

		int enrolmentId;
		Enrolment enrolment;
		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);

		object = new Activity();
		object.setTitle("");
		object.setSummary("");
		object.setType(Approach.THEORY_SESSION);
		object.setStartDate(null);
		object.setEndDate(null);
		object.setLink("");
		object.setEnrolment(enrolment);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, StudentActivityCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("endDate") && !super.getBuffer().getErrors().hasErrors("startDate")) {
			super.state(MomentHelper.isAfter(object.getEndDate(), object.getStartDate()), "endDate", "student.activity.form.error.endDate-too-soon");
			super.state(MomentHelper.isBeforeOrEqual(object.getEndDate(), MomentHelper.parse("yyyy-MM-dd-HH:mm", "2100-12-31-23:59")), "endDate", "student.activity.form.error.endDate-too-late");
			super.state(MomentHelper.isAfterOrEqual(object.getStartDate(), MomentHelper.parse("yyyy-MM-dd-HH:mm", "2000-01-01-00:00")), "startDate", "student.activity.form.error.startDate-too-soon");
		}
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamService.validateTextInput(object.getTitle()), "title", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(this.spamService.validateTextInput(object.getSummary()), "summary", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamService.validateTextInput(object.getLink()), "link", "activity.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamService.validateTextInput(object.getLink()), "link", "activity.error.spam");
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		final SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(Approach.class, object.getType());

		tuple = super.unbind(object, "title", "summary", "startDate", "endDate", "link");
		tuple.put("enrolmentId", super.getRequest().getData("enrolmentId", int.class));
		tuple.put("type", choices.getSelected().getKey());
		tuple.put("types", choices);
		super.getResponse().setData(tuple);
	}

}
