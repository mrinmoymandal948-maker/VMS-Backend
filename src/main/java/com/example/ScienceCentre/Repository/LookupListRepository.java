package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.LookupList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupListRepository extends JpaRepository<LookupList, Long> {

    @Query("""
        SELECT d FROM LookupList d
        WHERE d.header.lookupCode = :code
          AND d.activeFlag = 'Y'
        ORDER BY d.sortOrder ASC
    """)
    List<LookupList> findByLookupCode(@Param("code") String code);

    List<LookupList> findByParentLookupDtlIdAndActiveFlag(Long parentId, String activeFlag);
}
