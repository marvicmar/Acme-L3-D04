
package acme.features.authenticated.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import acme.services.SpamService;

@Service
public class AuthenticatedStudentCreateService extends AbstractService<Authenticated, Student> {

	// Constants --------------------------------------------------------------
	public static final String[]				PROPERTIES	= {
		"statement", "strongFeatures", "weakFeatures", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuthenticatedStudentRepository	repository;
	@Autowired
	protected SpamService						spamService;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Student.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Student object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Student();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Student object) {
		assert object != null;

		super.bind(object, AuthenticatedStudentCreateService.PROPERTIES);
	}

	@Override
	public void validate(final Student object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("statement"))
			super.state(this.spamService.validateTextInput(object.getStatement()), "statement", "student.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("strongFeatures"))
			super.state(this.spamService.validateTextInput(object.getStrongFeatures()), "strongFeatures", "student.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("weakFeatures"))
			super.state(this.spamService.validateTextInput(object.getStrongFeatures()), "weakFeatures", "student.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.spamService.validateTextInput(object.getLink()), "link", "student.error.spam");
	}

	@Override
	public void perform(final Student object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Student object) {
		Tuple tuple;

		tuple = super.unbind(object, AuthenticatedStudentCreateService.PROPERTIES);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
