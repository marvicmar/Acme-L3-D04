
package acme.testing.company.sessionPracticum;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanySessionPracticumCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionPracticumTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String abstractSession, final String description, final String start, final String end, final String link) {
		// HINT: this test authenticates as a company, list his or her practicums, navigates
		// HINT+ to their session practicums, and checks that they have the expected data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Session practicums");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.clickOnButton("Session practicums");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(sessionPracticumRecordIndex, 0, title);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 3, start);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 2, end);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 4, "No");

		super.clickOnListingRecord(0);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	void test101Positive() {
		// HINT: this test authenticates as a company, list his or her practicums, navigates
		// HINT+ to their session practicums, and checks that they have the expected data.

		final int practicumRecordIndex = 2;
		final int sessionPracticumRecordIndex = 0;
		final String title = "ZZZTitle";
		final String abstractSession = "ZZZAbstract";
		final String description = "ZZZDescription";
		final String start = "2022/12/12 12:00";
		final String end = "2023/12/12 13:00";
		final String link = "https://www.google.com/";

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("confirmed", "true");
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(sessionPracticumRecordIndex, 0, title);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 3, start);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 2, end);
		super.checkColumnHasValue(sessionPracticumRecordIndex, 4, "Yes");

		super.clickOnListingRecord(0);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session-practicum/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int sessionPracticumRecordIndex, final String title, final String abstractSession, final String description, final String start, final String end, final String link) {
		// HINT: this test attempts to create session practicums using wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test201Negative() {
		// HINT: this test attempts to create session practicums using wrong data.

		final int practicumRecordIndex = 1;

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");

		super.checkNotButtonExists("Create");

		super.signOut();
	}

	@Test
	void test202Negative() {
		// HINT: this test authenticates as a company, list his or her practicums, navigates
		// HINT+ to their session practicums, and checks that they have the expected data.

		final int practicumRecordIndex = 3;
		final String title = "ZZZTitle";
		final String abstractSession = "ZZZAbstract";
		final String description = "ZZZDescription";
		final String start = "2022/12/12 12:00";
		final String end = "2023/12/12 13:00";
		final String link = "https://www.google.com/";

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Session practicums");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("confirmed", "false");
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create a session practicum for a practicum as a principal without
		// HINT: the "Company" role.

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/session-practicum/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/session-practicum/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/session-practicum/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/session-practicum/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to create a session practicum for a published practicum created by
		// HINT+ the principal.

		Collection<Practicum> practicums;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("company1", "company1");
		practicums = this.repository.findManyPracticumsByCompanyUsernameInFinalMode("company1");
		for (final Practicum practicum : practicums) {
			boolean hasAdditional;

			hasAdditional = !this.repository.findManySessionPracticumsAdditionalByPracticumId(practicum.getId()).isEmpty();

			if (hasAdditional) {
				param = String.format("masterId=%d", practicum.getId());
				super.request("/company/session-practicum/create", param);
				super.checkPanicExists();
			}
		}
	}

}
