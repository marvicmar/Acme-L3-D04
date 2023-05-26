
package acme.testing.company.practicum;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

class CompanyPracticumDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;


	// Test data --------------------------------------------------------------
	@Test
	void test100Positive() {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT: practicums, selects one of them, and then deletes it.

		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(3, "asc");
		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete");
		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT: practicums, selects one of them, and then tries to delete it.

		super.signIn("company1", "company1");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(3, "desc");
		super.clickOnListingRecord(0);
		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to delete a practicum that is in draft mode
		// HINT: and tries to delete it with different roles.

		Collection<Practicum> practicums;
		String params;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.isDraftMode()) {
				params = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to delete a practicum that is not in draft mode
		// HINT: and tries to delete it.

		Collection<Practicum> practicums;
		String params;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (!practicum.isDraftMode()) {
				params = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test tries to delete a practicum from a
		// HINT: company that is not the owner of the practicum.

		Collection<Practicum> practicums;
		String params;

		super.signIn("company2", "company2");
		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			params = String.format("id=%d", practicum.getId());
			super.request("/company/practicum/delete", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
