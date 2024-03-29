package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChangeUserStatusRequest {
    @NotNull(message = "Required field")
    private UUID librarianId;

    @NotNull(message = "Required field")
    private Boolean activate;

    @NotNull(message = "Required field")
    private int tier;
}
