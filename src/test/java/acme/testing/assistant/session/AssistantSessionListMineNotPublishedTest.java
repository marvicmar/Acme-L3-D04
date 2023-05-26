
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionListMineNotPublishedTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/list-mine-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String code, final String title, final String type, final String published) {
		// HINT: this test authenticates as a assistant, then lists his or her tutorial,
		// HINT+ selects one of them, and check that it has the expected session tutorial.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my sessions");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, type);
		super.checkColumnHasValue(sessionRecordIndex, 2, code);
		super.checkColumnHasValue(sessionRecordIndex, 3, published);
		super.clickOnListingRecord(sessionRecordIndex);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to list the session practicums of a practicum that is unpublished
		// HINT+ using a principal that didn't create it.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialByAssistantUsernameInDraftMode("assistant1");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/list-mine", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/list-mine", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/assistant/session/list-mine", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/session/list-mine", param);
			super.checkPanicExists();
			super.signOut();

		}
	}
}
