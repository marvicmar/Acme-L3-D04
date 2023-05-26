
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

class AssistantTutorialPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialTestRepository repository;


	// Test data --------------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int tutorialRecordIndex) {
		// HINT: this test authenticates as a assistant, lists his or her tutorial,
		// HINT: then selects one of them, and publishes it.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(tutorialRecordIndex, 4, "No");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish Tutorial");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int tutorialRecordIndex) {
		// HINT: this test attempts to publish a assistant that cannot be published, yet.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(tutorialRecordIndex, 4, "Yes");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to publish a tutorial with a role other than "Assistant".

		Collection<Tutorial> tutorials;
		String params;

		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to publish a published tutorial that was registered by the principal.

		final Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (!tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	void test302Hacking() {
		// HINT: this test tries to publish a tutorial that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		final Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant2", "assistant2");
		tutorials = this.repository.findManyTutorialsByAssistantByUserName("assistant1");
		System.out.println(tutorials.size());
		for (final Tutorial tutorial : tutorials) {
			params = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/publish", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
