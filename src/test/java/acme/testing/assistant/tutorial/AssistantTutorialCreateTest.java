
package acme.testing.assistant.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

class AssistantTutorialCreateTest extends TestHarness {

	// Test data --------------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int assistantRecordIndex, final String code, final String course, final String title, final String summary, final String goals, final String estimatedTime) {
		// HINT: this test authenticates as a assistant and then lists his or her
		// HINT: tutorials, creates a new one, and check that it's been created properly.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();

		super.clickOnButton("Tutorial create");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.clickOnSubmit("Create Tutorial");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(assistantRecordIndex, 0, code);
		super.checkColumnHasValue(assistantRecordIndex, 1, title);
		super.checkColumnHasValue(assistantRecordIndex, 2, estimatedTime);
		super.checkColumnHasValue(assistantRecordIndex, 3, summary);
		super.clickOnListingRecord(assistantRecordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("summary", summary);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int assistantRecordIndex, final String code, final String course, final String title, final String summary, final String goals, final String estimatedTime) {
		// HINT: this test attempts to create tutorial with incorrect data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.clickOnButton("Tutorial create");
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTime", estimatedTime);
		super.clickOnSubmit("Create Tutorial");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create a tutorial using principals with
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

	}

}
