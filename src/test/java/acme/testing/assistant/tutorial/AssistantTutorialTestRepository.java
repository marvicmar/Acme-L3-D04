
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialTestRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.userAccount.username= ?1")
	Collection<Tutorial> findManyTutorialsByAssistantByUserName(String username);
}
