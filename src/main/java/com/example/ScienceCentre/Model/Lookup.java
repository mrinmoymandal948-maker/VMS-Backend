package com.example.ScienceCentre.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lookup")
public class Lookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lookup_id")
    private Long lookupId;

    @Column(name = "lookup_code", unique = true, nullable = false)
    private String lookupCode;

    @Column(name = "lookup_name")
    private String lookupName;

    @Column(name = "description")
    private String description;

    @Column(name = "active_flag", nullable = false, length = 1)
    private String activeFlag = "Y";
}
