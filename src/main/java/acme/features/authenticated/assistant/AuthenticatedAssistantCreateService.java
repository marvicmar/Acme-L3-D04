
package acme.features.authenticated.assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import acme.services.SpamService;

@Service
public class AuthenticatedAssistantCreateService extends AbstractService<Authenticated, Assistant> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuthenticatedAssistantRepository	repository;
	@Autowired
	protected SpamService						spamService;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Assistant object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Assistant();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Assistant object) {
		assert object != null;

		super.bind(object, "supervisor", "expertiseFields", "resume", "furtherInfo");

	}

	@Override
	public void validate(final Assistant object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("supervisor"))
			super.state(this.spamService.validateTextInput(object.getSupervisor()), "supervisor", "assistant.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("expertiseFields"))
			super.state(this.spamService.validateTextInput(object.getExpertiseFields()), "expertiseFields", "assistant.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("resume"))
			super.state(this.spamService.validateTextInput(object.getResume()), "resume", "assistant.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("furtherInfo"))
			super.state(this.spamService.validateTextInput(object.getFurtherInfo()), "furtherInfo", "assistant.error.spam");
	}

	@Override
	public void perform(final Assistant object) {
		assert object != null;
		this.repository.save(object);

	}

	@Override
	public void unbind(final Assistant object) {
		Tuple tuple;

		tuple = super.unbind(object, "supervisor", "expertiseFields", "resume", "furtherInfo");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();

	}
}
