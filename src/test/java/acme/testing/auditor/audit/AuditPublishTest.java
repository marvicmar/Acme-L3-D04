/*
 * EmployerJobPublishTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected AuditTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditRecordIndex, final String courseCode, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor2", "auditor2");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(auditRecordIndex, 1, code);

		super.clickOnListingRecord(auditRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Audit> audits;
		String params;

		audits = this.repository.findManyAuditsByFirmAndDraftMode("sergiosant1");
		for (final Audit audit : audits) {
			params = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/publish", params);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/publish", params);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/auditor/audit/publish", params);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/audit/publish", params);
			super.checkPanicExists();
			super.signOut();
		}

	}

}
