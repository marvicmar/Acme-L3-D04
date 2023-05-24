package acme.testing.company.practicum;

import acme.entities.practicum.Practicum;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CompanyPracticumTestRepository extends AbstractRepository {

    @Query("select p from Practicum p where p.company.userAccount.username = ?1")
    Collection<Practicum> findManyPracticumsByCompanyUsername(String username);
}
