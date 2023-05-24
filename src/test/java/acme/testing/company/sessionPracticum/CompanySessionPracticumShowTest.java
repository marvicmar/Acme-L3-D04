
package acme.testing.company.sessionPracticum;

import acme.entities.sessionPracticum.SessionPracticum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanySessionPracticumShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionPracticumTestRepository repository;


	// Test methods -----------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/company/session-practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String abstractSession, final String description, final String start, final String end, final String link) {
		// HINT: this test signs in as a company, lists his or her practicums, selects
		// HINT+ one of them and checks that it's as expected.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");
		super.checkListingExists();
		super.clickOnListingRecord(sessionPracticumRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}


	@Test
	void test300Hacking() {
		// HINT: this test tries to show a session practicum of a practicum that is in draft mode or
		// HINT+ not available, but wasn't published by the principal;

		Collection<SessionPracticum> sessionPracticums;
		String param;

		super.signIn("company1", "company1");
		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInDraftMode("company2");
		for (final SessionPracticum sessionPracticum : sessionPracticums) {
			param = String.format("id=%d", sessionPracticum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/session-practicum/show", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/session-practicum/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/session-practicum/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/session-practicum/show", param);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
