package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.activities.Activity;
import acme.testing.TestHarness;

public class StudentActivityDeleteTest extends TestHarness{

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentRecordIndex, final int activityRecordIndex) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List my enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities");
		super.checkListingExists();
		super.clickOnListingRecord(activityRecordIndex);
		super.checkFormExists();

		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();
		super.checkListingExists();

		super.signOut();
	}

	@Test
	public void test200Negative() {

	}

	@Test
	public void test300Hacking() {

		Collection<Activity> activities;
		String param;

		activities = this.repository.findActivitiesByStudentUsername("student1");
		for (final Activity activity : activities) {
			param = String.format("id=%d", activity.getEnrolment().getId());

			super.signIn("administrator", "administrator");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student2", "student2");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/student/activity/delete", param);
			super.checkPanicExists();
			super.signOut();
		}

	}

	@Test
	public void test301Hacking() {

		Collection<Activity> activities;
		String param;

		super.signIn("student1", "student1");
		activities = this.repository.findActivitiesByStudentUsername("student2");
		for (final Activity activity : activities)
			if (activity.getEnrolment().isDraftMode()) {
				param = String.format("id=%d", activity.getEnrolment().getId());

				super.checkLinkExists("Sign in");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
