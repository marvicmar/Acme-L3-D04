
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditListTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(auditRecordIndex, 0, courseCode);
		super.checkColumnHasValue(auditRecordIndex, 1, code);
		super.checkColumnHasValue(auditRecordIndex, 2, conclusion);
		super.checkColumnHasValue(auditRecordIndex, 3, strongPoints);
		super.checkColumnHasValue(auditRecordIndex, 4, weakPoints);

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

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/auditor/audit/show", param);
			super.checkPanicExists();
			super.signOut();

		}

	}
}
