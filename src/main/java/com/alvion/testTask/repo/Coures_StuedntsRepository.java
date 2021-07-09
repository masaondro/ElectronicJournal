package com.alvion.testTask.repo;

import com.alvion.testTask.models.Coures_Students;
import org.springframework.data.repository.CrudRepository;

public interface Coures_StuedntsRepository extends CrudRepository<Coures_Students, Long> {
    Iterable<Coures_Students> findAllByCourse(Long course);

    Iterable<Coures_Students> findAllByStudent(Long student);

    Iterable<Coures_Students> findAllByCourseAndStudent(Long course, Long student);
}
