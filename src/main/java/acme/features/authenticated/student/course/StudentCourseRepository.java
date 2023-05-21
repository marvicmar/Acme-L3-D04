
package acme.features.authenticated.student.course;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentCourseRepository extends AbstractRepository {

	@Query("select o from Student o")
	List<Student> findAllStudent();

	@Query("select o from Course o where o.draftMode=false")
	List<Course> findAllCourse();

	@Query("select o from Course o where o.id = :id")
	Course findCourseById(int id);

	@Query("select o from Student o where o.id = :id")
	Student findStudentById(int id);
}
