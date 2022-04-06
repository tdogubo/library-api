package com.etz.libraryapi.domains.responses;


import lombok.Getter;

@Getter
public class AppResponse<T> {
    private final Boolean status;
    private T data;
    private String message;

    public AppResponse(final Boolean status, final T data) {
        this.status = status;
        this.data = data;
    }

    public AppResponse(final Boolean status, final String message) {
        this.status = status;
        this.message = message;
    }

}
