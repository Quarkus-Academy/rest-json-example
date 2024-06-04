package com.ibm.quarkus.academy.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name="SimpleDto", description="POJO that represents a user.")
public class UserDto {
    @Schema( example = "1")
    String id;
    @Schema( example = "Adam")
    String name;
    @Schema( example = "Seidel")
    String surname;
    @Schema( example = "2000-01-01")
    LocalDate birthDate;
}
