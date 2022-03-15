package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CatalogRequest {
    @NotNull(message = "Required field")
    private UUID librarianId;
    @NotBlank(message = "Catalog required")
    private String name;
}