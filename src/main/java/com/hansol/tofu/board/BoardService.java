package com.hansol.tofu.board;

import com.hansol.tofu.board.domain.dto.BoardCreationRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardEditRequestDTO;
import com.hansol.tofu.board.repository.BoardRepository;
import com.hansol.tofu.clubphoto.ClubPhotoService;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
	private final ClubPhotoService photoService;

    public Long addBoard(Long clubId, BoardCreationRequestDTO boardCreationRequestDTO) {
        Long memberId = SecurityUtils.getCurrentUserId();
        var memberEntity = memberRepository.findMemberByIdAndMemberStatus(memberId, ACTIVATE)
                .orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

        photoService.savePhotos(boardCreationRequestDTO.clubPhotoRequestDTOs());
        return boardRepository.save(boardCreationRequestDTO.toEntity(memberEntity)).getId();
    }

    public void editBoard(Long clubId, Long boardId, BoardEditRequestDTO boardEditRequestDTO) {
        Long memberId = SecurityUtils.getCurrentUserId();
        var boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));

        if(!boardEntity.getMember().getId().equals(memberId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        boardEntity.changeBoard(boardEditRequestDTO.title(), boardEditRequestDTO.content());
        photoService.savePhotos(boardEditRequestDTO.clubPhotoRequestDTOs());
    }

    public void deleteBoard(Long clubId, Long boardId) {
        Long memberId = SecurityUtils.getCurrentUserId();
        var boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));

        if(!boardEntity.getMember().getId().equals(memberId)) {
            throw new BaseException(ACCESS_DENIED);
        }

        boardEntity.deleteBoard();
    }

}
