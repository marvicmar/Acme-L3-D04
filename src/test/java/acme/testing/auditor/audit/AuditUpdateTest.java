
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditUpdateTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditRecordIndex, final String courseCode, final String auditorFirm, final String codeAudit, final String conclusion, final String strongPoint, final String weakPoint) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 1, codeAudit);

		super.clickOnListingRecord(auditRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("conclusion", conclusion + "2.0");
		super.clickOnSubmit("Update");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, codeAudit);
		super.checkColumnHasValue(auditRecordIndex, 2, conclusion + "2.0");

		super.clickOnListingRecord(auditRecordIndex);
		super.checkInputBoxHasValue("course", courseCode);
		super.checkInputBoxHasValue("code", codeAudit);
		super.checkInputBoxHasValue("auditor.firm", auditorFirm);
		super.checkInputBoxHasValue("conclusion", conclusion + "2.0");

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final String courseCode, final String auditorFirm, final String codeAudit, final String conclusion, final String strongPoint, final String weakPoint) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 1, codeAudit);

		super.clickOnListingRecord(auditRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("conclusion",
			conclusion + "dddgggggggggggggggggggggjjjjjjjjjjjjjjjjjjjjjojnvjfnfpijnpigjnbpigjnbjgnbgjbnbgjnbgjnbjgnbjgbgggggggggggggggggddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		super.fillInputBoxIn("weakPoints", "");
		super.clickOnSubmit("Update");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByFirm("employer1");

		for (final Audit audit : audits) {

			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/audit/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/audit/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
