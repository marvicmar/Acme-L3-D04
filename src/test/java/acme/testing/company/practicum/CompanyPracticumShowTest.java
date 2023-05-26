
package acme.testing.company.practicum;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class CompanyPracticumShowTest extends TestHarness {

    // Internal state ---------------------------------------------------------
    @Autowired
    protected CompanyPracticumTestRepository repository;


    // Test data --------------------------------------------------------------

    @ParameterizedTest

    @CsvFileSource(resources = "/company/practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    void test100Positive(final int practicumRecordIndex, final String course, final String code, final String title, final String abstractPracticum, final String goals, final String estimatedTimeInHours) {
        // HINT: this test signs in as a company, lists all the practicums, click on
        // HINT+ one of them, and checks that the form has the expected data.

        super.signIn("company1", "company1");

        super.clickOnMenu("Company", "List all practicums");
        super.sortListing(0, "asc");
        super.clickOnListingRecord(practicumRecordIndex);
        super.checkFormExists();

        super.checkFormExists();
        super.checkInputBoxHasValue("course", course);
        super.checkInputBoxHasValue("code", code);
        super.checkInputBoxHasValue("title", title);
        super.checkInputBoxHasValue("abstractPracticum", abstractPracticum);
        super.checkInputBoxHasValue("goals", goals);
        super.checkInputBoxHasValue("estimatedTimeInHours", estimatedTimeInHours);

        super.signOut();
    }

    @Test
    void test200Negative() {
        // HINT: there aren't any negative tests for this feature because it's a listing
        // HINT+ that doesn't involve entering any data in any forms.
    }


    @Test
    void test300Hacking() {
        // HINT: this test tries to show an unpublished job by someone who is not the principal.

        Collection<Practicum> practicums;
        String param;

        practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
        for (final Practicum practicum : practicums)
            if (practicum.isDraftMode()) {
                param = String.format("id=%d", practicum.getId());

                super.checkLinkExists("Sign in");
                super.request("/company/practicum/show", param);
                super.checkPanicExists();

                super.signIn("administrator", "administrator");
                super.request("/company/practicum/show", param);
                super.checkPanicExists();
                super.signOut();

                super.signIn("company2", "company2");
                super.request("/company/practicum/show", param);
                super.checkPanicExists();
                super.signOut();

                super.signIn("student1", "student1");
                super.request("/company/practicum/show", param);
                super.checkPanicExists();
                super.signOut();
            }
    }
}
