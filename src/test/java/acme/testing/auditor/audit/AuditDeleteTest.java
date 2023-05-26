
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditTestRepository repository;


	// Test data --------------------------------------------------------------
	@Test
	void test100Positive() {

		super.signIn("auditor2", "auditor2");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "asc");
		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete");

		super.signOut();
		super.signIn("auditor2", "auditor2");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "asc");
		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete");

		super.signOut();
	}

	@Test
	void test200Negative() {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "desc");
		super.clickOnListingRecord(0);
		super.checkNotButtonExists("Delete");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "desc");
		super.clickOnListingRecord(1);
		super.checkNotButtonExists("Delete");

		super.signOut();
	}

	@Test
	void test300Hacking() {

		Collection<Audit> audits;
		String params;

		audits = this.repository.findManyAuditsByFirmAndDraftMode("auditor2");
		for (final Audit audit : audits) {

			params = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/delete", params);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/delete", params);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/audit/delete", params);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
