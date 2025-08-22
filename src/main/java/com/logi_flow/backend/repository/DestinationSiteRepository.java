package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DestinationSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationSiteRepository extends JpaRepository<DestinationSite, Long> {
}
