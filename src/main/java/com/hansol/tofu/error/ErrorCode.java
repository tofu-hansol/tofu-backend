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
	DUPLICATED_APPLICANT(HttpStatus.CONFLICT, "이미 신청한 모임입니다"),
	ALREADY_REQUESTED_JOIN_CLUB(HttpStatus.CONFLICT, "이미 가입요청한 모임입니다"),
	ALREADY_JOINED_CLUB(HttpStatus.CONFLICT, "이미 가입된 모임입니다"),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),


	NOT_FOUND_DEPT(HttpStatus.NOT_FOUND, "부서정보를 찾을 수 없습니다"),
	NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "회사정보를 찾을 수 없습니다"),
	NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리 정보를 찾을 수 없습니다"),
	NOT_FOUND_CLUB(HttpStatus.NOT_FOUND, "동호회 정보를 찾을 수 없습니다"),
	NOT_FOUND_CLUB_MEMBER(HttpStatus.NOT_FOUND, "해당 동호회에 가입 혹은 가입요청된 회원 정보를 찾을 수 없습니다"),
	NOT_FOUND_CLUB_SCHEDULE(HttpStatus.NOT_FOUND, "동호회 모임일정 정보를 찾을 수 없습니다"),
	NOT_FOUND_APPLICANT(HttpStatus.NOT_FOUND, "동호회 모임일정 신청자 정보를 찾을 수 없습니다"),
	NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "게시판 정보를 찾을 수 없습니다"),
	NOT_FOUND_CLUB_PHOTO(HttpStatus.NOT_FOUND, "동호회 사진 정보를 찾을 수 없습니다"),
	NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글 정보를 찾을 수 없습니다"),


	FAILED_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패하였습니다"),


	ALREADY_STARTED(HttpStatus.BAD_REQUEST, "이미 시작한 모임일정입니다"),
	EXCEED_MAX_APPLICANT(HttpStatus.BAD_REQUEST, "최대 참여 인원을 초과한 랠리 스케쥴입니다"),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
	NOT_ACTIVATE_MEMBER(HttpStatus.BAD_REQUEST, "비활성화된 회원입니다"),
	FAILED_TO_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패하였습니다"),
	INVALID_FILETYPE(HttpStatus.INTERNAL_SERVER_ERROR, "파일형식이 잘못되었습니다");


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
