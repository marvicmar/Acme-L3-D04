
package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

class AnyPeepShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int peepRecordIndex, final String instantiationMoment, final String title, final String nickname, final String message, final String email, final String url) {

		super.clickOnMenu("Any", "List all peeps");
		super.sortListing(1, "asc");
		super.clickOnListingRecord(peepRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("moment", instantiationMoment);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nickname);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", url);
	}

}
