
package acme.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spam.filter.SpamFilter;

import acme.repositories.SpamRepository;

@Service
public class SpamService {

	// Internal state ---------------------------------------------------------
	@Autowired
	private SpamRepository repository;


	// Methods -----------------------------------------------------------------
	public boolean validateTextInput(final String input) {
		String spamWords;
		double spamThreshold;

		spamWords = this.repository.findSpamWords();
		spamThreshold = this.repository.findSpamThreshold();

		final SpamFilter spamFilter = new SpamFilter(spamWords, spamThreshold, "-");
		return input == null || !spamFilter.isSpam(input);
	}
}
