
package acme.testing.assistant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialListAllTest extends TestHarness {

	// Test data --------------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/list-all-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int tutorialRecordIndex, final String code, final String title, final String estimatedTime, final String summary) {
		// HINT: this test authenticates as a assistant and then lists his or her tutorials
		// HINT+ tutorial and check that they are properly listed.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List all tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.checkColumnHasValue(tutorialRecordIndex, 1, title);
		super.checkColumnHasValue(tutorialRecordIndex, 2, estimatedTime);
		super.checkColumnHasValue(tutorialRecordIndex, 3, summary);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	void test300Positive() {
		// HINT: this test tries to list all the tutorials using
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/assistant/tutorial/list-all");
		super.checkPanicExists();
		super.signOut();
	}
}
