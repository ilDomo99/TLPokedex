package com.tl.pokedex.constant;

public class ErrorConstant {
    public static final String GENERIC_EXCEPTION_LOG = "An unexpected error occurred: ";
    public static final String GENERIC_EXCEPTION_MESSAGE = "Oops, something went wrong";

    public static final String BAD_REQUEST_INVALID_VALUE_LOG = "Validation error at path [{}] in class [{}] method [{}] caused by [{}]: (invalidValue [{}], reason [{}])";
    public static final String BAD_REQUEST_INVALID_VALUE_MESSAGE = "invalid value [%s], reason [%s]";

    public static final String OBJECT_CANNOT_BE_NULL_MESSAGE = "object cannot be null";

    public static final String VALIDATION_ERROR_MESSAGE = "Error during validation of [%s] caused by: [%s]";
}
