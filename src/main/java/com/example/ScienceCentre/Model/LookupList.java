package com.example.ScienceCentre.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lookup_list")
public class LookupList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lookup_dtl_id")
    @JsonProperty("lookupDtlId")
    private Long lookupDtlId;

    @Column(name = "lookup_value", nullable = false)
    @JsonProperty("lookupValue")
    private String lookupValue;

    @Column(name = "lookup_label", nullable = false)
    @JsonProperty("lookupLabel")
    private String lookupLabel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lookup_id", nullable = false)
    @JsonIgnore
    private Lookup header;

    @Column(name = "parent_lookup_dtl_id")
    private Long parentLookupDtlId;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "active_flag", nullable = false, length = 1)
    private String activeFlag = "Y";
}
