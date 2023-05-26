
package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

class AnyPeepListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int peepRecordIndex, final String moment, final String title, final String nick) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Any", "List all peeps");
		super.checkListingExists();
		super.sortListing(1, "asc");

		super.checkColumnHasValue(peepRecordIndex, 0, moment);
		super.checkColumnHasValue(peepRecordIndex, 1, title);
		super.checkColumnHasValue(peepRecordIndex, 2, nick);

		super.signOut();
	}
}
