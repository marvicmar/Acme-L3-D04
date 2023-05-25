
package acme.testing.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.framework.repositories.AbstractRepository;

public interface AuditTestRepository extends AbstractRepository {

	@Query("select audit from Audit audit where audit.draftMode = true")
	Collection<Audit> findManyAuditsInDraftMode();

	@Query("select audit from Audit audit where audit.auditor.firm = :firm")
	Collection<Audit> findManyAuditsByFirm(String firm);

	@Query("select audit from Audit audit where audit.auditor.firm = :firm and audit.draftMode = true")
	Collection<Audit> findManyAuditsByFirmAndDraftMode(String firm);

	@Query("select count(a) from AuditingRecord a WHERE a.audit.code=:code")
	int findSizeOfAudit(String code);

}
