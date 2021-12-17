package be.intec.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {

    USER_INFO_REQUIRED("User info is required."),
    USER_ID_IS_REQUIRED("User ID required!"),
    USER_NAME_IS_REQUIRED("User name required!"),
    USER_EMAIL_IS_REQUIRED("User email required!"),
    USER_AGE_IS_REQUIRED("User age required!"),
    SEARCH_KEYWORD_IS_REQUIRED("Search keyword is required!"),
    SEARCH_CRITERIA_IS_REQUIRED("Search criteria (minAge, maxAge) are required!"),

    USER_ID_IS_NOT_VALID("User ID is NOT valid!"),
    USER_ID_MUST_NOT_BE_INITIALIZED("User ID must NOT be initialized!"),
    USER_NAME_IS_NOT_VALID("User name is NOT valid!"),
    USER_EMAIL_IS_NOT_VALID("User email is NOT valid!"),
    USER_AGE_IS_NOT_VALID("User age is NOT valid!"),
    SEARCH_KEYWORD_IS_NOT_VALID("Search keyword is NOT valid!"),
    SEARCH_CRITERIA_IS_NOT_VALID("Search criteria(s) is/are NOT valid!"),

    USER_NOT_FOUND("User NOT found!"),
    USER_ALREADY_EXISTS("User with this email already exist!"),
    UNDEFINED_EXCEPTION("There has been an undefined issue while processing information! Please try again.");

    private final String body;


}
