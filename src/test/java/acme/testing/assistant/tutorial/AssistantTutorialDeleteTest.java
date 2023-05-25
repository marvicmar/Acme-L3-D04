
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

class AssistantTutorialDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialTestRepository repository;


	// Test data --------------------------------------------------------------
	@Test
	void test100Positive() {
		// HINT: this test authenticates as a assistant and then lists his or her
		// HINT: tutorial, selects one of them, and then deletes it.

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();

		super.sortListing(3, "asc");
		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete Tutorial");
		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: this test authenticates as a asssistant and then lists his or her
		// HINT: tutorial, selects one of them, and then tries to delete it.

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();

		super.sortListing(3, "desc");
		super.clickOnListingRecord(0);
		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to delete a tutorial that is in draft mode
		// HINT: and tries to delete it with different roles.

		Collection<Tutorial> tutorials;
		String params;

		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to delete a tutorial that is not in draft mode
		// HINT: and tries to delete it.

		Collection<Tutorial> tutorials;
		String params;

		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (!tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test tries to delete a practicum from a
		// HINT: company that is not the owner of the practicum.

		Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant2", "assistant2");
		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		for (final Tutorial tutorial : tutorials) {
			params = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/delete", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
