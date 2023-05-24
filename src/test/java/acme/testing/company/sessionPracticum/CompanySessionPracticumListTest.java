
package acme.testing.company.sessionPracticum;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanySessionPracticumListTest extends TestHarness {

    // Internal state ---------------------------------------------------------
    @Autowired
    protected CompanySessionPracticumTestRepository repository;


    // Test data --------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/company/session-practicum/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void test100Positive(final int practicumRecordIndex, final String code, final int sessionPracticumRecordIndex, final String title, final String exactDuration, final String start, final String end, final String additional) {
        // HINT: this test authenticates as a company, then lists his or her practicums,
        // HINT+ selects one of them, and check that it has the expected session practicums.

        super.signIn("company1", "company1");

        super.clickOnMenu("Company", "List my practicums");
        super.checkListingExists();
        super.sortListing(0, "desc");

        super.checkColumnHasValue(practicumRecordIndex, 0, code);
        super.clickOnListingRecord(practicumRecordIndex);
        super.checkInputBoxHasValue("code", code);
        super.clickOnButton("Session practicums");

        super.checkListingExists();
        super.checkColumnHasValue(sessionPracticumRecordIndex, 0, title);
        super.checkColumnHasValue(sessionPracticumRecordIndex, 1, exactDuration);
        super.checkColumnHasValue(sessionPracticumRecordIndex, 2, end);
        super.checkColumnHasValue(sessionPracticumRecordIndex, 3, start);
        super.checkColumnHasValue(sessionPracticumRecordIndex, 4, additional);
        super.clickOnListingRecord(sessionPracticumRecordIndex);

        super.signOut();
    }

    @Test
    void test200Negative() {
        // HINT: there's no negative test case for this listing, since it doesn't
        // HINT+ involve filling in any forms.
    }


    @Test
    void test300Hacking() {
        // HINT: this test tries to list the session practicums of a practicum that is unpublished
        // HINT+ using a principal that didn't create it.

        Collection<Practicum> practicums;
        String param;

        practicums = this.repository.findManyPracticumsByCompanyUsernameInDraftMode("company1");
        for (final Practicum practicum : practicums) {
            param = String.format("masterId=%d", practicum.getId());

            super.checkLinkExists("Sign in");
            super.request("/company/session-practicum/list", param);
            super.checkPanicExists();

            super.signIn("administrator", "administrator");
            super.request("/company/session-practicum/list", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("company2", "company2");
            super.request("/company/session-practicum/list", param);
            super.checkPanicExists();
            super.signOut();

            super.signIn("student1", "student1");
            super.request("/company/session-practicum/list", param);
            super.checkPanicExists();
            super.signOut();
        }
    }
}
