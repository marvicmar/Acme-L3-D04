
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.practicum.Practicum;
import acme.entities.sessionPracticum.SessionPracticum;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyPracticumRepository extends AbstractRepository {

	@Query("select a from Company a where a.id = ?1")
	Company findOneCompanyById(int activeRoleId);

	@Query("select a from Practicum a where a.id = ?1")
	Practicum findOnePracticumById(int masterId);

	@Query("select sp from SessionPracticum sp where sp.practicum.id = ?1")
	Collection<SessionPracticum> findManySessionPracticesByPracticumId(int id);

	@Query("select a from Practicum a where a.company.id = ?1")
	Collection<Practicum> findManyPracticumsByCompanyId(int activeRoleId);

	@Query("select a from Practicum a where a.draftMode = false")
	Collection<Practicum> findManyPracticums();

	@Query("select a from Course a where a.id = ?1")
	Course findOneCourseById(int courseId);

	@Query("select a from Course a")
	Collection<Course> findAllCourses();

	@Query("select p from Practicum p where p.code = ?1")
	Collection<Practicum> findManyPracticumByCode(String code);
}
