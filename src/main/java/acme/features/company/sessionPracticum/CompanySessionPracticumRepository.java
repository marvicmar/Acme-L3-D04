
package acme.features.company.sessionPracticum;

import acme.entities.practicum.Practicum;
import acme.entities.sessionPracticum.SessionPracticum;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CompanySessionPracticumRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.id = ?1")
	Practicum findOnePracticumById(int practicumId);

	@Query("select s from SessionPracticum s where s.id = ?1")
	SessionPracticum findOneSessionPracticumById(int sessionPracticumId);

	@Query("select s from SessionPracticum s where s.practicum.id = ?1")
	Collection<SessionPracticum> findManySessionPracticumsByPracticumId(int practicumId);

	@Query("select sp.practicum from SessionPracticum sp where sp.id = ?1")
	Practicum findOnePracticumBySessionPracticumId(int sessionPracticumId);

	@Query("select sp from SessionPracticum sp where sp.practicum.id = ?1 and sp.additional = true")
	Collection<SessionPracticum> findManySessionPracticumsByExtraAvailableAndPracticumId(int id);
}
