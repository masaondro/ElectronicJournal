package com.alvion.testTask.repo;

import com.alvion.testTask.models.Courses;
import org.springframework.data.repository.CrudRepository;

public interface CoursesRepository extends CrudRepository<Courses, Long> {

    Iterable<Courses> findAllByProfessor(Long professor);
}
