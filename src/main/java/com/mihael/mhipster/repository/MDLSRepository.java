package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.MDLS;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MDLS entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MDLSRepository extends JpaRepository<MDLS, Long> {
    @Query("select mDLS from MDLS mDLS where mDLS.user.login = ?#{authentication.name}")
    List<MDLS> findByUserIsCurrentUser();
}
