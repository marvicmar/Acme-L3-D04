
package acme.features.authenticated.note;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.note.Note;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedNoteShowService extends AbstractService<Authenticated, Note> {

	// Constants -------------------------------------------------------------
	protected static final String[]			PROPERTIES	= {
		"moment", "title", "author", "message", "emailAddress", "link"
	};

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedNoteRepository	repository;

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
		final Note note;
		final Date deadline;

		id = super.getRequest().getData("id", int.class);
		note = this.repository.findOneNoteById(id);
		deadline = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);
		status = MomentHelper.isAfter(note.getMoment(), deadline);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Note note;
		int id;

		id = super.getRequest().getData("id", int.class);
		note = this.repository.findOneNoteById(id);

		super.getBuffer().setData(note);
	}

	@Override
	public void unbind(final Note note) {
		assert note != null;

		final Tuple tuple;

		tuple = super.unbind(note, AuthenticatedNoteShowService.PROPERTIES);

		super.getResponse().setData(tuple);
	}
}
