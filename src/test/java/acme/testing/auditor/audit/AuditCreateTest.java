
package acme.testing.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditCreateTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditRecordIndex, final String courseCode, final String auditorFirm, final String codeAudit, final String conclusion, final String strongPoint, final String weakPoint) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.clickOnButton("Register");
		super.fillInputBoxIn("course", courseCode);
		super.fillInputBoxIn("code", codeAudit);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoint);
		super.fillInputBoxIn("weakPoints", weakPoint);
		super.clickOnSubmit("Register");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, codeAudit);

		super.clickOnListingRecord(auditRecordIndex);
		super.checkInputBoxHasValue("course", courseCode);
		super.checkInputBoxHasValue("code", codeAudit);
		super.checkInputBoxHasValue("auditor.firm", auditorFirm);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final String courseCode, final String auditorFirm, final String codeAudit, final String conclusion, final String strongPoint, final String weakPoint) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.clickOnButton("Register");
		super.fillInputBoxIn("course", courseCode);
		super.fillInputBoxIn("code", codeAudit);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoint);
		super.fillInputBoxIn("weakPoints", weakPoint);
		super.clickOnSubmit("Register");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/audit/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/auditor/audit/create");
		super.checkPanicExists();
		super.signOut();
	}

}
