
package acme.testing.auditor.auditing_record;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditingRecordListTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditingRecordTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final int auditingRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String subject, final String assessment,
		final String start, final String end, final String duration, final String mark) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);
		super.checkColumnHasValue(auditRecordIndex, 2, conclusion);
		super.checkColumnHasValue(auditRecordIndex, 3, strongPoints);
		super.checkColumnHasValue(auditRecordIndex, 4, weakPoints);

		super.clickOnListingRecord(auditRecordIndex);

		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Records");

		super.checkListingExists();
		super.checkColumnHasValue(auditingRecordIndex, 0, subject);
		super.checkColumnHasValue(auditingRecordIndex, 1, assessment);
		super.checkColumnHasValue(auditingRecordIndex, 2, start);
		super.checkColumnHasValue(auditingRecordIndex, 3, end);
		super.checkColumnHasValue(auditingRecordIndex, 4, duration);
		super.checkColumnHasValue(auditingRecordIndex, 5, mark);

		super.signOut();
	}

	@Test
	void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByFirmAndDraftMode("sergiosant1");
		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/list", param);
			super.checkPanicExists();
			super.request("/authenticated/auditing-record/list", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/list", param);
			super.checkPanicExists();
			super.request("/authenticated/auditing-record/list", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/auditor/auditing-record/list", param);
			super.checkPanicExists();
			super.request("/authenticated/auditing-record/list", param);
			super.checkPanicExists();
			super.signOut();

		}

	}
}
