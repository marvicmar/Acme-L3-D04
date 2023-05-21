
package acme.features.administrator.bulletin;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.bulletin.Bulletin;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.services.SpamService;

@Service
public class AdministratorBulletinCreateService extends AbstractService<Administrator, Bulletin> {

	// Constants -------------------------------------------------------------
	protected static final String[]				PROPERTIES	= {
		"moment", "title", "message", "flags", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBulletinRepository	repository;
	@Autowired
	protected SpamService						spamDetector;

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
		Bulletin object;
		object = new Bulletin();

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Bulletin object) {
		assert object != null;

		super.bind(object, AdministratorBulletinCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Bulletin object) {
		assert object != null;

		boolean confirmation;
		Date currentMoment;

		if (!super.getBuffer().getErrors().hasErrors("confirmation")) {
			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
		}
		if (!super.getBuffer().getErrors().hasErrors("moment")) {
			currentMoment = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(currentMoment, object.getMoment()), "moment", "javax.validation.constraints.Date");
		}

		// Spam validation
		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.spamDetector.validateTextInput(object.getTitle()), "title", "administrator.bulletin.form.error.spam.title");
		if (!super.getBuffer().getErrors().hasErrors("message"))
			super.state(this.spamDetector.validateTextInput(object.getMessage()), "message", "administrator.bulletin.form.error.spam.message");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamDetector.validateTextInput(object.getLink()), "link", "administrator.bulletin.form.error.spam.link");

	}

	@Override
	public void perform(final Bulletin object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Bulletin object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, AdministratorBulletinCreateService.PROPERTIES);
		tuple.put("confirmation", false);
		super.getResponse().setData(tuple);
	}
}
