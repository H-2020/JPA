package be.intec.services.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {

    STUDENT_INFO_REQUIRED("Student info is required."),
    STUDENT_ID_IS_REQUIRED("Student ID required!"),
    STUDENT_NAME_IS_REQUIRED("Student name required!"),
    STUDENT_EMAIL_IS_REQUIRED("Student email required!"),
    STUDENT_AGE_IS_REQUIRED("Student age required!"),
    SEARCH_KEYWORD_IS_REQUIRED("Search keyword is required!"),
    SEARCH_CRITERIA_IS_REQUIRED("Search criteria (minAge, maxAge) are required!"),

    STUDENT_ID_IS_NOT_VALID("Student ID is NOT valid!"),
    STUDENT_ID_MUST_NOT_BE_INITIALIZED("Student ID must NOT be initialized!"),
    STUDENT_NAME_IS_NOT_VALID("Student name is NOT valid!"),
    STUDENT_EMAIL_IS_NOT_VALID("Student email is NOT valid!"),
    STUDENT_AGE_IS_NOT_VALID("Student age is NOT valid!"),
    SEARCH_KEYWORD_IS_NOT_VALID("Search keyword is NOT valid!"),
    SEARCH_CRITERIA_IS_NOT_VALID("Search criteria(s) is/are NOT valid!"),

    STUDENT_NOT_FOUND("Student NOT found!"),
    STUDENT_ALREADY_EXISTS("Student with this email already exist!"),
    UNDEFINED_EXCEPTION("There has been an undefined issue while processing information! Please try again.");

    private final String body;
}
