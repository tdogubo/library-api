package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class GenericDeleteRequest {
    @NotNull(message = "Required field")
    private UUID librarianId;
}
