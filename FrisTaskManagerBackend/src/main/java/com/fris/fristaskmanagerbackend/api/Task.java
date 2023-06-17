package com.fris.fristaskmanagerbackend.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Task {
    @JsonProperty
    private long id;

    @JsonProperty
    private String tittle;

    @JsonProperty
    private Date date;

}
