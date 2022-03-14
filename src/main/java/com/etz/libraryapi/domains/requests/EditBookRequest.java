package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class EditBookRequest {
    @NotNull(message = "Title can not be null")
    @NotEmpty(message = "Title can not be empty")
    private String title;
    @NotNull(message = "Can be empty array and not null")
    private List<String> addAuthor;
    @NotNull(message = "delete cannot be null")
    private List<String> deleteAuthor;
    @NotNull(message = "Description can not be null")
    @NotEmpty
    private String description;
    private int copies = 0;
}
