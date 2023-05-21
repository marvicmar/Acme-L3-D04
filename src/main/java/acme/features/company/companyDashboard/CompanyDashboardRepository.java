
package acme.features.company.companyDashboard;

import java.util.Date;
import java.util.List;

import acme.entities.enums.Approach;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyDashboardRepository extends AbstractRepository {

	@Query("select c from Company c where c.userAccount.id = ?1")
	Company findOneCompanyByUserAccountId(int userAccountId);

	// Calcula las estadísticas de las sesiones de una empresa
	@Query("select count(sp) from SessionPracticum sp where sp.practicum.company.id = ?1")
	int findCountSession(int companyId);

	@Query("select avg(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1")
	double findAverageSessionLength(int companyId);

	@Query("select stddev(datediff(sp.end,sp.start)) from SessionPracticum  sp where sp.practicum.company.id = ?1")
	double findDeviationSessionLength(int companyId);

	@Query("select min(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1")
	double findMinimumSessionLength(int companyId);

	@Query("select max(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1")
	double findMaximumSessionLength(int companyId);

	// Calcula las estadísticas de las prácticas de una empresa
	@Query("select avg((select sum(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findAveragePracticaLength(int companyId);

	@Query("select stddev((select sum(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findDeviationPracticaLength(int companyId);

	@Query("select min((select sum(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findMinimumPracticaLength(int companyId);

	@Query("select max((select sum(datediff(sp.end,sp.start)) from SessionPracticum sp where sp.practicum.company.id = ?1 and sp.practicum.id = p.id)) from Practicum p where p.company.id = ?1")
	double findMaximumPracticaLength(int companyId);

	@Query("select count(p) from Practicum p where p.company.id = ?1")
	int findCountPractica(int companyId);

	@Query("SELECT MONTH(sp.start), COUNT(sp) FROM SessionPracticum sp WHERE sp.practicum.course.type = ?3 and sp.practicum.company.id = ?1 and sp.end <= ?2 GROUP BY MONTH(sp.start) ORDER BY COUNT(sp) DESC")
	List<Object[]> findTotalNumberOfPracticaByMonth(int companyId, Date currentMoment, Approach type);

}
