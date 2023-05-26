package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentPublishTest extends TestHarness{

	@Autowired
	protected StudentEnrolmentTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String cardHolderName, final String cardNumber, final String expirationDate, final String cvv) {
		// HINT: this test authenticates as an student, lists his or her enrolments, and then selects one of them, and publishes it.

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List my enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("cardHolderName", cardHolderName);
		super.fillInputBoxIn("cardNumber", cardNumber);
		super.fillInputBoxIn("expirationDate", expirationDate);
		super.fillInputBoxIn("cvv", cvv);
		super.clickOnSubmit("Publish");
		
		super.checkNotErrorsExist();

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnButton("Activities");
		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String cardHolderName, final String cardNumber, final String expirationDate, final String cvv) {
		// HINT: this test attempts to publish an enrolment that cannot be published, yet.

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List my enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		
		super.checkColumnHasValue(recordIndex, 0, code);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		
		super.fillInputBoxIn("cardHolderName", cardHolderName);
		super.fillInputBoxIn("cardNumber", cardNumber);
		super.fillInputBoxIn("expirationDate", expirationDate);
		super.fillInputBoxIn("cvv", cvv);
		super.clickOnSubmit("Publish");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish an enrolment with a role other than "Student".

		Collection<Enrolment> enrolments;
		String params;

		enrolments = this.repository.findEnrolmentsByUsernameStudent("student1");
		for (final Enrolment enrolment : enrolments)
			if (enrolment.isDraftMode()) {
				params = String.format("id=%d", enrolment.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published enrolment that was registered by the principal.

		Collection<Enrolment> enrolments;
		String params;

		super.signIn("student1", "student1");
		enrolments = this.repository.findEnrolmentsByUsernameStudent("student1");
		for (final Enrolment enrolment : enrolments)
			if (!enrolment.isDraftMode()) {
				params = String.format("id=%d", enrolment.getId());
				super.request("/student/enrolment/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish an enrolment that wasn't registered by the principal, be it published or unpublished.

		Collection<Enrolment> enrolments;
		String params;

		super.signIn("student2", "student2");
		enrolments = this.repository.findEnrolmentsByUsernameStudent("student1");
		for (final Enrolment enrolment : enrolments) {
			params = String.format("id=%d", enrolment.getId());
			super.request("/student/enrolment/publish", params);
		}
		super.signOut();
	}
	
}
