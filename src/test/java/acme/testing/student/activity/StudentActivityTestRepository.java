
package acme.testing.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.activities.Activity;
import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;

public interface StudentActivityTestRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.student.userAccount.username = :username")
	Collection<Enrolment> findEnrolmentsByStudentUsername(String username);

	@Query("select a from Activity a where a.enrolment.student.userAccount.username = :username")
	Collection<Activity> findActivitiesByStudentUsername(String username);

}
