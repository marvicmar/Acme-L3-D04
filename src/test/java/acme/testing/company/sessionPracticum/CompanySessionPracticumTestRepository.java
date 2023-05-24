package acme.testing.company.sessionPracticum;

import acme.entities.practicum.Practicum;
import acme.entities.sessionPracticum.SessionPracticum;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CompanySessionPracticumTestRepository extends AbstractRepository {

    @Query("select p from Practicum p where p.company.userAccount.username = ?1")
    Collection<Practicum> findManyPracticumsByCompanyUsername(String username);

    // Método que permita obtener las sesiones de una práctica
    @Query("select sp from SessionPracticum sp where sp.practicum.id = ?1 and sp.additional = true")
    Collection<SessionPracticum> findManySessionPracticumsAdditionalByPracticumId(int id);

    @Query("select sp from SessionPracticum sp where sp.practicum.company.userAccount.username = ?1 and sp.practicum.draftMode = true")
    Collection<SessionPracticum> findManySessionPracticumsByCompanyUsernameInDraftMode(String username);

    @Query("select sp from SessionPracticum sp where sp.practicum.company.userAccount.username = ?1 and sp.practicum.draftMode = false")
    Collection<SessionPracticum> findManySessionPracticumsByCompanyUsernameInFinalMode(String username);

    @Query("select sp from SessionPracticum sp where sp.practicum.company.userAccount.username = ?1")
    Collection<SessionPracticum> findManySessionPracticumsByCompanyUsername(String username);


    @Query("select p from Practicum p where p.company.userAccount.username = ?1 and p.draftMode = true")
    Collection<Practicum> findManyPracticumsByCompanyUsernameInDraftMode(String username);

    @Query("select p from Practicum p where p.company.userAccount.username = ?1 and p.draftMode = false")
    Collection<Practicum> findManyPracticumsByCompanyUsernameInFinalMode(String username);
}
