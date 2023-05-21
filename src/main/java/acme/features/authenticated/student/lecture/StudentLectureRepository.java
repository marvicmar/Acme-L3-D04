
package acme.features.authenticated.student.lecture;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface StudentLectureRepository extends AbstractRepository {

	@Query("select cl.lecture from CourseLecture cl where cl.course.id=:courseId")
	List<Lecture> findLecturesByCourseId(int courseId);

	@Query("select c from Course c where c.id=:courseId")
	Course findCourseById(int courseId);
}
