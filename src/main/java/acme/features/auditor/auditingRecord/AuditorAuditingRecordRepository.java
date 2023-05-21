
package acme.features.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.audit_record.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditingRecordRepository extends AbstractRepository {

	@Query("select a from AuditingRecord a where a.id = :id")
	AuditingRecord findOneAuditingRecordById(int id);

	@Query("select a from Audit a where a.id = :id")
	Audit findOneAuditByAuditId(int id);

	@Query("select aR.audit from AuditingRecord aR where aR.id = :id")
	Audit findOneAuditByAuditingRecordId(int id);

	@Query("select a from AuditingRecord a WHERE a.audit.id=:id")
	Collection<AuditingRecord> findAuditingRecordsByAuditId(int id);

}
