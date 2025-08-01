package com.codeit.hrbank.exception;

import lombok.Getter;

public enum ExceptionCode {
    DEPARTMENT_HAS_EMPLOYEE_CANNOT_DELETE(400, "소속 직원이 있는 부서는 삭제할 수 없습니다.", "details"),
    DEPARTMENT_ID_IS_NOT_FOUND(400, "잘못된 요청입니다.", "details"),
    STORED_FILE_NOT_FOUND(404,"잘못된 요청입니다.","해당 이미지를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(400, "잘못된 요청입니다.", "해당 이메일은 이미 존재합니다."),
    EMPLOYEE_NOT_FOUND(404, "잘못된 요청입니다.", "해당 직원을 찾을 수 없습니다."),
    STOREDFILE_NOT_FOUND(404, "잘못된 요청입니다.", "해당 파일을 찾을 수 없습니다.");
//    USER_NOT_FOUND(404, "User Not Found"),
//    EMAIL_OR_USERNAME_ALREADY_EXISTS(400, "User With Email Already Exists"),
//    CHANNEL_OR_USER_NOT_FOUND(404, "Channel | User With ID Not Found"),
//    READSTATUS_ALREADY_EXISTS(400, "ReadStatus With UserId and ChannelId Already Exists"),
//    WRONG_PASSWORD(400, "Wrong Password"),
//    USERSTATUS_NOT_FOUND(404, "UserStatus Not Found"),
//    READSTATUS_NOT_FOUND(404, "Message ReadStatus Not Found"),
//    MESSAGE_NOT_FOUND(404, "Message Not Found"),
//    CHANNEL_NOT_FOUND(404, "Channel Not Found"),
//    PRIVATE_CHANNEL_CANNOT_UPDATE(400, "Private Channel Cannot Be Updated"),
//    BINARY_CONTENT_NOT_FOUND(404, "BinaryContent Not Found"),
//
//    BINARY_CONTENT_EXISTS(400, "BinaryContent Exists"),
//    USER_ALREADY_EXISTS_USERSTATUS(400, "User Already Exists UserStatus"),
//    INVALID_PAST_TIME(400, "Time Is Earlier Than Saved Time"),
//    BINARY_CONTENT_STORAGE_NOT_FOUND(404, "BinaryContent Storage Not Found");

    @Getter
    private final int status;

    @Getter
    private final String message;

    @Getter
    private final String details;

    ExceptionCode(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
