
package acme.features.assistant.assistantDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.session.Session;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantDashboardRepository extends AbstractRepository {

	@Query("select a from Assistant a where a.userAccount.id = ?1")
	Assistant findOneAssistantByUserAccountId(int userAccountId);

	@Query("select avg(datediff(s.end,s.start)) from Session s where s.tutorial.assistant.id = ?1")
	double findAverageSessionLength(int assistantId);

	@Query("select stddev(datediff(s.end,s.start)) from Session s where s.tutorial.assistant.id = ?1")
	double findDeviationSessionLength(int assistantId);

	@Query("select min(datediff(s.end,s.start)) from Session s where s.tutorial.assistant.id = ?1")
	double findMinimumSessionLength(int assistantId);

	@Query("select max(datediff(s.end,s.start)) from Session s where s.tutorial.assistant.id = ?1")
	double findMaximumSessionLength(int assistantId);

	@Query("select count(s) from Session s where s.tutorial.assistant.id = ?1")
	int findCountSession(int assistantId);

	@Query("select avg(t.estimatedTime) from Tutorial t where t.assistant.id = ?1")
	double findAverageTutorialLength(int assistantId);

	@Query("select stddev(t.estimatedTime) from Tutorial t where t.assistant.id = ?1")
	double findDeviationTutorialLength(int assistantId);

	@Query("select min(t.estimatedTime) from Tutorial t where t.assistant.id = ?1")
	double findMinimumTutorialLength(int assistantId);

	@Query("select max(t.estimatedTime) from Tutorial t where t.assistant.id = ?1")
	double findMaximumTutorialLength(int assistantId);

	@Query("select count(t) from Tutorial t where t.assistant.id = ?1")
	int findCountTutorial(int assistantId);

	@Query("SELECT count(t) FROM Tutorial t WHERE t.assistant.id = ?1")
	Long findTotalNumberOfTutorial(int assistantId);

	@Query("SELECT s FROM Session s WHERE s.tutorial.id = :id")
	Collection<Session> findManySessionsByTutorialId(int id);

}
