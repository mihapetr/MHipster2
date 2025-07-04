package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProjectRepositoryWithBagRelationships {
    Optional<Project> fetchBagRelationships(Optional<Project> project);

    List<Project> fetchBagRelationships(List<Project> projects);

    Page<Project> fetchBagRelationships(Page<Project> projects);
}
