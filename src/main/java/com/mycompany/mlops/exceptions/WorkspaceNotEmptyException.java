package com.mycompany.mlops.exceptions;

public class WorkspaceNotEmptyException extends RuntimeException {
    public WorkspaceNotEmptyException(String message) {
        super(message);
    }
}