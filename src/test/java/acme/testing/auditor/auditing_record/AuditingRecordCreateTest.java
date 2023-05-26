
package acme.testing.auditor.auditing_record;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditingRecordCreateTest extends TestHarness {

	@Autowired
	protected AuditingRecordTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
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

		super.clickOnButton("Register");

		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Register");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);

		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Records");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(auditingRecordIndex);

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditing-record/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
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

		super.clickOnButton("Register");

		super.checkFormExists();

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Register");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByFirm("sergiosant1");
		for (final Audit audit : audits) {

			param = String.format("auditId=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/auditing-record/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/auditing-record/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/auditing-record/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/auditing-record/create", param);
			super.checkPanicExists();
			super.signOut();

		}
	}

}
