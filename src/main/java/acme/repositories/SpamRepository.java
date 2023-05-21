
package acme.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface SpamRepository extends AbstractRepository {

	@Query("select c.spamWords from Configuration c")
	String findSpamWords();

	@Query("select c.spamThreshold from Configuration c")
	Double findSpamThreshold();

}
