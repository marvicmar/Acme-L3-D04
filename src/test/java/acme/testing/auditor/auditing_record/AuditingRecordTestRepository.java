
package acme.testing.auditor.auditing_record;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

public interface AuditingRecordTestRepository extends AbstractRepository {

	@Query("select audit from Audit audit where audit.draftMode = true")
	Collection<Audit> findManyAuditsInDraftMode();

	@Query("select auditing from AuditingRecord auditing where (auditing.audit.draftMode = true and auditing.audit.auditor.firm = :firm)")
	Collection<AuditingRecord> findManyAuditingRecordsInDraftModeAndFirm(String firm);

	@Query("select audit from Audit audit where audit.auditor.firm = :firm")
	Collection<Audit> findManyAuditsByFirm(String firm);

	@Query("select audit from Audit audit where audit.auditor.firm = :firm and audit.draftMode = true")
	Collection<Audit> findManyAuditsByFirmAndDraftMode(String firm);
}
