
package acme.testing.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantSessionTestRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.userAccount.username = ?1")
	Collection<Tutorial> findManytutorialByAssistantUsername(String username);

	// Método que permita obtener las sesiones de un tutorial
	@Query("select s from Session s where s.tutorial.id = ?1 and s.draftMode = true")
	Collection<Session> findManySessionAdditionalByTutorialId(int id);

	@Query("select s from Session s where s.tutorial.assistant.userAccount.username = ?1 and s.tutorial.draftMode = true")
	Collection<Session> findManySessionByAssistantUsernameInDraftMode(String username);

	@Query("select s from Session s where s.tutorial.assistant.userAccount.username = ?1 and s.tutorial.draftMode = false")
	Collection<Session> findManySessionByAssistantUsernameFinalMode(String username);

	@Query("select s from Session s where s.tutorial.assistant.userAccount.username = ?1")
	Collection<Session> findManySessionByAssistantUsername(String username);

	@Query("select t from Tutorial t where t.assistant.userAccount.username = ?1 and t.draftMode = true")
	Collection<Tutorial> findManyTutorialByAssistantUsernameInDraftMode(String username);

	@Query("select t from Tutorial t where t.assistant.userAccount.username = ?1 and t.draftMode = false")
	Collection<Tutorial> findManyTutorialByAssistantUsernameInFinalMode(String username);

}
