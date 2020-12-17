package org.example.exceptions;

public enum ErrorCode {
    /**
     * Unknown
     */
    UNKNOWN,
    /**
     * Input invalid
     */
    INPUT_NOT_VALID,
    /**
     * Parsing error (json parsing, datetime parsing etc.)
     */
    PARSING_ERROR,
    /**
     * Missing requested entity in db
     */
    MISSING_ENTITY,
    /**
     * Duplicate entity
     */
    DUPLICATE_ENTITY,
    /**
     * Failed to delete entity
     */
    FAILED_TO_DELETE,
    /**
     * Internal server error
     */
    SERVICE_FAILED,
    /**
     * Remote server error
     */
    REMOTE_ERROR,
    /**
     * Failed computation
     */
    COMPUTATION_ERROR,
    /**
     * Not implemented
     */
    NOT_IMPLEMENTED,
    /**
     * Unreachable (shouldn't happen) Serious error.
     */
    UNREACHABLE,
    /**
     * Reached max iteration number
     */
    MAX_ITERATION_REACHED,

    REMOTE_LOGIN,

    TOKEN_INVALID
}