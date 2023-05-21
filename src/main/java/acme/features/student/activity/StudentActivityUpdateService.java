
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.enums.Approach;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import acme.services.SpamService;

@Service
public class StudentActivityUpdateService extends AbstractService<Student, Activity> {

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

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int activityId;
		Activity activity;

		activityId = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(activityId);
		status = activity != null && !activity.getEnrolment().isDraftMode() && super.getRequest().getPrincipal().getAccountId() == activity.getEnrolment().getStudent().getUserAccount().getId();

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

		super.bind(object, StudentActivityCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("endPeriod") && !super.getBuffer().getErrors().hasErrors("startPeriod"))
			super.state(MomentHelper.isAfter(object.getEndDate(), object.getStartDate()), "endPeriod", "student.activity.form.error.endPeriod-too-soon");
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

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(Approach.class, object.getType());

		tuple = super.unbind(object, StudentActivityDeleteService.PROPERTIES);
		tuple.put("types", choices);
		tuple.put("readonly", object.getEnrolment().isDraftMode());

		super.getResponse().setData(tuple);
	}

}
