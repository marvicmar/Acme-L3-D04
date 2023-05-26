
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

class CompanyPracticumListMineTest extends TestHarness {

	// Test data --------------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/list-mine-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final String code, final String estimatedTimeInHours, final String title, final String published) {
		// HINT: this test authenticates as a company, lists his or her practicums only,
		// HINT+ and then checks that the listing has the expected data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.checkColumnHasValue(practicumRecordIndex, 1, estimatedTimeInHours);
		super.checkColumnHasValue(practicumRecordIndex, 2, title);
		super.checkColumnHasValue(practicumRecordIndex, 3, published);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to list all the practicums using
		// HINT+ from a user with the wrong role.

		super.checkLinkExists("Sign in");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();
	}
}
