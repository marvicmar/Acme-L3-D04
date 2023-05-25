
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

class AssistantSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@Test
	void test100Positive() {
		// HINT: this test authenticates as a assistant, list his or her session
		// HINT: , and checks that they have the expected data.

		final int tutorialRecordIndex = 2;
		final String title = "TTTTTitle";
		final String summary = "SummarySessionSSS";
		final String type = "THEORY_SESSION";
		final String startDate = "2023/05/13 02:02";
		final String endDate = "2023/05/13 05:00";
		final String link = "https://www.google.com/";

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "List all tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Session Create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("start", startDate);
		super.fillInputBoxIn("end", endDate);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create Session");

		super.clickOnMenu("Assistant", "List my sessions");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(0);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("summary", summary);
		super.checkInputBoxHasValue("type", type);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("endDate", endDate);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: this test attempts to create session tutorial using wrong data.

		final int tutorialRecordIndex = 2;
		final String title = "TTTTTitle";
		final String summary = "SummarySessionSSS";
		final String type = "THEORY_SESSION";
		final String startDate = "2023/07/12 12:00";
		final String endDate = "2023/07/13 14:00";
		final String link = "https://www.google.com/";

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "List all tutorials");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Session Create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("start", startDate);
		super.fillInputBoxIn("end", endDate);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create Session");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create a session practicum for a practicum as a principal without
		// HINT: the "Company" role.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManytutorialByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("masterId=%d", tutorial.getId());

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

			super.signIn("assistant2", "assistant2");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
