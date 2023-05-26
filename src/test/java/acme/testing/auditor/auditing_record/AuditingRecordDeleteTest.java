
package acme.testing.auditor.auditing_record;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditRecord.AuditingRecord;
import acme.testing.TestHarness;

public class AuditingRecordDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditingRecordTestRepository repository;

	//Test data--------------------------------------------------------------


	@Test
	void test100Positive() {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "asc");
		super.clickOnListingRecord(0);

		super.clickOnButton("Records");

		super.clickOnListingRecord(0);
		super.clickOnSubmit("Delete");

		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.sortListing(1, "asc");
		super.clickOnListingRecord(0);

		super.clickOnButton("Records");

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

		super.clickOnButton("Records");

		super.clickOnListingRecord(0);
		super.checkNotButtonExists("Delete");

		super.signOut();
	}

	@Test
	void test300Hacking() {

		Collection<AuditingRecord> records;
		String params;

		records = this.repository.findManyAuditingRecordsInDraftModeAndFirm("auditor2");
		for (final AuditingRecord record : records) {

			params = String.format("id=%d", record.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/delete", params);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/delete", params);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/auditing-record/delete", params);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/delete", params);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
