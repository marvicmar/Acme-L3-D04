
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.session.Session;
import acme.testing.TestHarness;

class AssistantSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionTestRepository repository;


	// Test data --------------------------------------------------------------
	@Test
	void test100Positive() {
		// HINT: this test authenticates as a assistant and then lists his or her
		// HINT+ sessions, selects one of them, navigates to the session tutorials
		// HINT+ and then deletes one of them.

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List my sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(0);

		super.clickOnSubmit("Delete Session");
		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: this test authenticates as a assistant and then lists his or her
		// HINT+ sessions, selects one of them, navigates to the session 
		// HINT+ and then tries to delete one of them.

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List all tutorials");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("Session List");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.checkNotButtonExists("Delete Session");
		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test authenticates as a assistant and tries to delete a session
		// HINT+ that is in draft mode..

		Collection<Session> sessions;
		String params;

		sessions = this.repository.findManySessionByAssistantUsernameInDraftMode("company1");
		for (final Session session : sessions)
			if (!session.isDraftMode()) {
				params = String.format("id=%d", session.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/session/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/session/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/session/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/assistant/session/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test authenticates as a assistant and tries to delete a session
		// HINT+ that isn't in draft 

		Collection<Session> sessions;
		String params;

		sessions = this.repository.findManySessionByAssistantUsernameFinalMode("company1");
		for (final Session session : sessions)
			if (session.isDraftMode()) {
				params = String.format("id=%d", session.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test authenticates as a assistant and tries to delete a session
		// HINT+ from another assistant.

		Collection<Session> sessions;
		String params;

		super.signIn("company2", "company2");
		sessions = this.repository.findManySessionByAssistantUsername("company1");
		for (final Session session : sessions) {
			params = String.format("id=%d", session.getId());
			super.request("/company/session-practicum/delete", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
