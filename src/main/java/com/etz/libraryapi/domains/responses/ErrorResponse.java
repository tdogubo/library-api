package com.etz.libraryapi.domains.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private Boolean status = false;
    private Object data;
}
