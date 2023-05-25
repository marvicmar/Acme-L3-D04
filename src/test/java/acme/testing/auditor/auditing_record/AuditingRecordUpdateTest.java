
package acme.testing.auditor.auditing_record;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit_record.AuditingRecord;
import acme.testing.TestHarness;

public class AuditingRecordUpdateTest extends TestHarness {

	@Autowired
	protected AuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final int auditingRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String subject, final String assessment,
		final String start, final String end, final String mark, final String link) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);

		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Records");
		super.checkListingExists();

		super.clickOnListingRecord(auditingRecordIndex);

		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("mark", mark);

		super.clickOnSubmit("Update");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);

		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Records");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditingRecordIndex);

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("mark", mark);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final int auditingRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String subject,
		final String assessment, final String start, final String end, final String mark, final String link) {
		// HINT: this test attempts to create duties using wrong data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);

		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Records");
		super.checkListingExists();

		super.clickOnListingRecord(auditingRecordIndex);

		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Update");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<AuditingRecord> records;
		String param;

		records = this.repository.findManyAuditingRecordsInDraftModeAndFirm("sergiosant1");

		for (final AuditingRecord record : records) {

			param = String.format("id=%d", record.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
