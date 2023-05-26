
package acme.testing.company.sessionPracticum;

import acme.entities.sessionPracticum.SessionPracticum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanySessionPracticumUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionPracticumTestRepository repository;
	// Test data --------------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/company/session-practicum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String abstractSession, final String description, final String start, final String end, final String link) {
		// HINT: this test logs in as a company,
		// HINT+ lists his or her practicums, navigates
		// HINT+ to their session practicums, and
		// HINT+ selects one of them, updates it, and then checks that
		// HINT+ the update has actually been performed.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Session practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(sessionPracticumRecordIndex);

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Session practicums");
		super.checkListingExists();

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(sessionPracticumRecordIndex, 0, title);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 2, end);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 3, start);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 4, "No");

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

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String abstractSession, final String description, final String start, final String end, final String link) {
		// HINT: this test attempts to update a session practicum with wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Session practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(sessionPracticumRecordIndex);

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}


	@Test
	void test300Hacking() {
		// HINT: this test tries to update a session practicum that isn't additional
		// HINT+ with a role other than "Company" or using an employer who is not the owner.

		Collection<SessionPracticum> sessionPracticums;
		String param;

		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInDraftMode("company1");
		for (final SessionPracticum sessionPracticum : sessionPracticums)
			if (!sessionPracticum.isAdditional()) {
				param = String.format("id=%d", sessionPracticum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to update a session practicum that is additional
		// HINT+ with a role other than "Company" or using an employer who is not the owner.

		Collection<SessionPracticum> sessionPracticums;
		String param;

		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInFinalMode("company1");
		for (final SessionPracticum sessionPracticum : sessionPracticums)
			if (sessionPracticum.isAdditional()) {
				param = String.format("id=%d", sessionPracticum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/session-practicum/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test tries to update a session practicum that his practicum is not on draft
		// HINT+ mode with a role other than "Company", with the owner or using an employer who is not the owner.

		Collection<SessionPracticum> sessionPracticums;
		String param;

		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInFinalMode("company1");
		for (final SessionPracticum sessionPracticum : sessionPracticums) {
			param = String.format("id=%d", sessionPracticum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/session-practicum/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/session-practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/session-practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/session-practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/company/session-practicum/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
