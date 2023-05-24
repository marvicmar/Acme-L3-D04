
package acme.testing.company.practicum;

import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class CompanyPracticumListAllTest extends TestHarness {


    // Test data --------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/company/practicum/list-all-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void test100Positive(final int practicumRecordIndex, final String code, final String estimatedTimeInHours, final String title) {
        // HINT: this test authenticates as a company and then lists his or her
        // HINT+ practicums and check that they are properly listed.


        super.signIn("company1", "company1");

        super.clickOnMenu("Company", "List all practicums");

        super.checkListingExists();
        super.sortListing(0, "asc");

        super.checkColumnHasValue(practicumRecordIndex, 0, code);
        super.checkColumnHasValue(practicumRecordIndex, 1, estimatedTimeInHours);
        super.checkColumnHasValue(practicumRecordIndex, 2, title);

        super.signOut();
    }

    @Test
    void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }


    @Test
    void test300Positive() {
        // HINT: this test tries to list all the practicums using
        // HINT+ inappropriate roles.

        super.checkLinkExists("Sign in");
        super.request("/company/practicum/list-all");
        super.checkPanicExists();

        super.signIn("administrator", "administrator");
        super.request("/company/practicum/list-all");
        super.checkPanicExists();
        super.signOut();

        super.signIn("student1", "student1");
        super.request("/company/practicum/list-all");
        super.checkPanicExists();
        super.signOut();
    }
}
