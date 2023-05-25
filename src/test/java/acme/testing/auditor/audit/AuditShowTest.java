
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditShowTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;

	//Test methods-----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final String courseCode, final String auditorFirm, final String codeAudit, final String conclusion, final String strongPoint, final String weakPoint) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.clickOnListingRecord(auditRecordIndex);

		super.checkFormExists();

		super.checkInputBoxHasValue("course", courseCode);
		super.checkInputBoxHasValue("auditor.firm", auditorFirm);
		super.checkInputBoxHasValue("code", codeAudit);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoint);
		super.checkInputBoxHasValue("weakPoints", weakPoint);

		super.signOut();
	}

	@Test
	void test300Hacking() {

		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsInDraftMode();
		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.request("/authenticated/audit/show", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.request("/authenticated/audit/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.request("/authenticated/audit/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.request("/authenticated/audit/show", param);
			super.checkPanicExists();
			super.signOut();
		}

	}

}
