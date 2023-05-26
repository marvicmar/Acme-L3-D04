
package acme.testing.company.practicum;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanyPracticumUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;


	// Test methods ------------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final String course, final String code, final String title, final String abstractPracticum, final String goals, final String estimatedTimeInHours) {
		// HINT: this test logs in as a company, lists his or her practicums,
		// HINT+ selects one of them, updates it, and then checks that
		// HINT+ the update has actually been performed.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		// super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTimeInHours", estimatedTimeInHours);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.clickOnMenu("Company", "List my practicums");
		super.sortListing(2, "desc");

		super.checkColumnHasValue(0, 0, code);
		super.checkColumnHasValue(0, 1, estimatedTimeInHours);
		super.checkColumnHasValue(0, 2, title);
		super.checkColumnHasValue(0, 3, "No");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractPracticum", abstractPracticum);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTimeInHours", estimatedTimeInHours);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final String course, final String code, final String title, final String abstractPracticum, final String goals, final String estimatedTimeInHours) {
		// HINT: this test attempts to update a practicum with wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(2, "desc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("estimatedTimeInHours", estimatedTimeInHours);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to update a practicum with a role other than "Company",
		// HINT+ or using a company who is not the owner.

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("id=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
