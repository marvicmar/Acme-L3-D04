
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantSessionRepository extends AbstractRepository {

	@Query("SELECT s FROM Session s WHERE s.id = ?1")
	Session findOneSessionById(int id);

	@Query("SELECT a FROM Assistant a WHERE a.id = ?1")
	Assistant findOneAssistantById(int id);

	@Query("SELECT t FROM Tutorial t WHERE t.id = ?1")
	Tutorial findOneTutorialById(int id);

	@Query("SELECT t FROM Tutorial t")
	Collection<Tutorial> findAllTutorial();

	@Query("SELECT s FROM Session s WHERE s.tutorial.id= ?1")
	Collection<Session> findManySessionByTutorialId(int id);

	@Query("SELECT s FROM Session s WHERE s.draftMode = false")
	Collection<Session> findManySession();

}
