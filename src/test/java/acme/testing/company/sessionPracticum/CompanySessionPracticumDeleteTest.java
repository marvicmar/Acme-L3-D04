
package acme.testing.company.sessionPracticum;

import acme.entities.sessionPracticum.SessionPracticum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanySessionPracticumDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionPracticumTestRepository repository;


	// Test data --------------------------------------------------------------

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int sessionPracticumRecordIndex) {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT+ practicums, selects one of them, navigates to the session practicums
		// HINT+ and then deletes one of them.

		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(3, "asc");
		super.clickOnListingRecord(practicumRecordIndex);

		super.clickOnButton("Session practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");

		super.clickOnListingRecord(sessionPracticumRecordIndex);

		super.clickOnSubmit("Delete");
		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int sessionPracticumRecordIndex) {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT+ practicums, selects one of them, navigates to the session practicums
		// HINT+ and then tries to delete one of them.

		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(3, "desc");
		super.clickOnListingRecord(practicumRecordIndex);

		super.clickOnButton("Session practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(sessionPracticumRecordIndex);

		super.checkNotButtonExists("Delete");
		super.signOut();
	}


	@Test
	void test300Hacking() {
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ practicum that is in draft mode..

		Collection<SessionPracticum> sessionPracticums;
		String params;

		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInDraftMode("company1");
		System.out.println(sessionPracticums);
		for (final SessionPracticum sessionPracticum : sessionPracticums)
			if (!sessionPracticum.isAdditional()) {
				params = String.format("id=%d", sessionPracticum.getId());

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

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ practicum that isn't in draft mode and is additional.

		Collection<SessionPracticum> sessionPracticums;
		String params;

		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsernameInFinalMode("company1");
		for (final SessionPracticum sessionPracticum : sessionPracticums)
			if (sessionPracticum.isAdditional()) {
				params = String.format("id=%d", sessionPracticum.getId());

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
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ from another company.

		Collection<SessionPracticum> sessionPracticums;
		String params;

		super.signIn("company2", "company2");
		sessionPracticums = this.repository.findManySessionPracticumsByCompanyUsername("company1");
		for (final SessionPracticum sessionPracticum : sessionPracticums) {
			params = String.format("id=%d", sessionPracticum.getId());
			super.request("/company/session-practicum/delete", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
