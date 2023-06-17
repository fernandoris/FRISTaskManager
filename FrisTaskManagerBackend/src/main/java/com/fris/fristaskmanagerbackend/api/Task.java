package com.fris.fristaskmanagerbackend.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Task {

    @JsonProperty
    private String tittle;

    @JsonProperty
    private Date date;

}
