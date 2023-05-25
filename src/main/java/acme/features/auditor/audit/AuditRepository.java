
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.audit_record.AuditingRecord;
import acme.entities.courses.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditRepository extends AbstractRepository {

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

	@Query("select c from Course c where c.id=:courseId")
	Course findOneCourseById(int courseId);

	@Query("select course from Course course")
	Collection<Course> findAllCourses();

	@Query("select count(a) = 0 from Audit a where a.code = :code")
	Boolean isUniqueCodeAudit(String code);

	@Query("select a from AuditingRecord a WHERE a.audit.id=:id")
	Collection<AuditingRecord> findAuditingRecordsByAuditId(int id);

	@Query("select count(a) from AuditingRecord a WHERE a.audit.id=:id")
	int findSizeOfAudit(int id);

}
