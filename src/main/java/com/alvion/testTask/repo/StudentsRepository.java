package com.alvion.testTask.repo;

import com.alvion.testTask.models.Students;
import org.springframework.data.repository.CrudRepository;

public interface StudentsRepository extends CrudRepository<Students, Long> {

}
