
package acme.features.authenticated.auditingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedAuditingRecordRepository extends AbstractRepository {

	@Query("select a from AuditingRecord a where a.id = :id and a.audit.draftMode = false")
	AuditingRecord findOneAuditingRecordById(int id);

	@Query("select a from Audit a where a.id = :id and a.draftMode = false")
	Audit findOneAuditByAuditId(int id);

	@Query("select a from AuditingRecord a WHERE a.audit.id=:id and a.audit.draftMode = false")
	Collection<AuditingRecord> findAuditingRecordsByAuditId(int id);

}
