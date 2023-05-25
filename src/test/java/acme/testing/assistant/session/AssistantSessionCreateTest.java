
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

class AssistantSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String summary, final String type, final String start, final String end, final String link) {
		// HINT: this test authenticates as a company, list his or her practicums, navigates
		// HINT+ to their session practicums, and checks that they have the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Session Create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create Session");

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String summary, final String type, final String start, final String end, final String link) {
		// HINT: this test attempts to create session practicums using wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Session create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create Session");

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create a session tutorial for a tutorial as a principal without
		// HINT: the "assistant" role.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManytutorialByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
