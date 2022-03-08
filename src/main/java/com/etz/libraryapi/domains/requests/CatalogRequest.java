package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CatalogRequest {
    @NotBlank
    private String name;
}