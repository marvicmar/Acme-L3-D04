
package acme.features.authenticated.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.courses.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuthenticatedAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditById(int id);

	@Query("select audit from Audit audit where audit.course.id = :id")
	Collection<Audit> findAuditsByCourse(int id);

	@Query("select audit from Audit audit where audit.draftMode = false")
	Collection<Audit> findAuditsPublish();

	@Query("select audit from Audit audit where audit.auditor.userAccount.id = :id")
	Collection<Audit> findAuditsByAuditor(int id);

	@Query("select a from Auditor a where a.userAccount.id = :id")
	Auditor findOneAuditorByUserAccountId(int id);

	@Query("select c from Course c where c.code = :code")
	Course findOneCurseByCode(String code);

	@Query("select course from Course course")
	Collection<Course> findCourses();

	@Query("select count(a) = 0 from Audit a where a.code = :code")
	Boolean isUniqueCodeAudit(String code);

}
