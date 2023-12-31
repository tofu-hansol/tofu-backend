package com.hansol.tofu.board;

import com.hansol.tofu.board.domain.dto.BoardCreationRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardEditRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardResponseDTO;
import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.board.repository.BoardRepository;
import com.hansol.tofu.clubphoto.ClubPhotoService;
import com.hansol.tofu.clubphoto.domain.dto.ClubPhotoResponseDTO;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.global.SecurityUtils;
import com.hansol.tofu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.hansol.tofu.error.ErrorCode.*;
import static com.hansol.tofu.member.enums.MemberStatus.ACTIVATE;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class BoardService {

	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final ClubPhotoService photoService;

	@Transactional(readOnly = true)
	public List<ClubPhotoResponseDTO> getClubPhotos(Long clubId) {
		List<Long> boardIds = boardRepository.findBoardIdListByClubId(clubId, BoardStatus.DELETED);

		return photoService.findClubPhotoByBoardIdIn(boardIds).stream()
			.map(ClubPhotoResponseDTO::of)
			.sorted((o1, o2) -> o2.createdAt().compareTo(o1.createdAt()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<BoardResponseDTO> getFeaturedBoardPages(Pageable pageable) {
		return boardRepository.findFeaturedBoardPages(pageable);
	}

	@Transactional(readOnly = true)
	public Page<BoardResponseDTO> getClubBoardPages(Long clubId, Pageable pageable) {
		return boardRepository.findClubBoardPages(clubId, pageable);
	}

	public Long addBoard(Long clubId, BoardCreationRequestDTO boardCreationRequestDTO, List<MultipartFile> clubPhotoRequestDTOs) {
		Long memberId = SecurityUtils.getCurrentUserId();

		var memberEntity = memberRepository.findMemberByIdAndMemberStatus(memberId, ACTIVATE)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));

		var boardEntity = boardRepository.save(boardCreationRequestDTO.toEntity(memberEntity, clubId));

		if (clubPhotoRequestDTOs != null && !clubPhotoRequestDTOs.isEmpty()) {
			photoService.createPhotos(boardEntity, clubPhotoRequestDTOs);
		}

		return boardEntity.getId();
	}

	 public void editBoard(Long boardId, BoardEditRequestDTO boardEditRequestDTO) {
	 	Long memberId = SecurityUtils.getCurrentUserId();
	 	var boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));

	 	if (!boardEntity.getMember().getId().equals(memberId)) {
	 		throw new BaseException(ACCESS_DENIED);
	 	}

	 	boardEntity.changeBoard(boardEditRequestDTO.title(), boardEditRequestDTO.content());
		//	 	photoService.savePhotos(boardEditRequestDTO.clubPhotoRequestDTOs());
	 }

	public void deleteBoard(Long clubId, Long boardId) {
		Long memberId = SecurityUtils.getCurrentUserId();
		var boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new BaseException(NOT_FOUND_BOARD));

		if (!boardEntity.getMember().getId().equals(memberId)) {
			throw new BaseException(ACCESS_DENIED);
		}

		boardEntity.deleteBoard();
	}

}
