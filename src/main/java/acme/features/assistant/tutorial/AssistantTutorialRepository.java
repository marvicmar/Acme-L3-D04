
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.session.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.id = ?1")
	Tutorial findOneTutorialById(int id);

	@Query("select a from Assistant a where a.id = ?1")
	Assistant findOneAssistantById(int id);

	@Query("SELECT c FROM Course c WHERE c.id = ?1")
	Course findOneCourseById(int id);

	@Query("SELECT c FROM Course c")
	Collection<Course> findAllCourses();

	@Query("select t from Tutorial t where t.draftMode = false")
	Collection<Tutorial> findManyTutorial();

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.id = ?1")
	Collection<Tutorial> findManyTutorialsByAssistantId(int id);

	@Query("SELECT s FROM Session s WHERE s.tutorial.id = ?1")
	Collection<Session> findManySessionsByTutorialId(int id);

	@Query("select t from Tutorial t where t.code = ?1")
	Tutorial findOneTutorialByCode(String code);

}
