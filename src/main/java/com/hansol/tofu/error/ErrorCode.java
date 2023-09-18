package com.hansol.tofu.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	FAILED_KAKAO_AUTH(HttpStatus.UNAUTHORIZED, "카카오 인증에 실패하였습니다"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다"),
	NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "해당하는 토큰을 찾을 수 없습니다"),
	NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "해당 유저를 찾을 수 없습니다"),
	PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다"),


	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
	DUPLICATE_MEMBER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),


	NOT_FOUND_DEPT(HttpStatus.NOT_FOUND, "부서정보를 찾을 수 없습니다"),
	NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "회사정보를 찾을 수 없습니다"),


	FAILED_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패하였습니다"),


	ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 참여 요청한 랠리 스케쥴입니다"),
	ALREADY_STARTED(HttpStatus.BAD_REQUEST, "이미 시작한 랠리 스케쥴입니다"),
	EXCEED_MAX_APPLICANT(HttpStatus.BAD_REQUEST, "최대 참여 인원을 초과한 랠리 스케쥴입니다"),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
	NOT_ACTIVATE_MEMBER(HttpStatus.BAD_REQUEST, "비활성화된 회원입니다"),;


	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
