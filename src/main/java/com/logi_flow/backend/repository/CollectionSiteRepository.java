package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.CollectionSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionSiteRepository extends JpaRepository<CollectionSite, Long> {
}
