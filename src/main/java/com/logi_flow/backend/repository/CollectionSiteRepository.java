package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.CollectionSite;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollectionSiteRepository extends JpaRepository<CollectionSite, Long> {

    Optional<CollectionSite> findByPhoneNumber(String phoneNumber);
}
