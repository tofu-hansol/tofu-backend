package com.hansol.tofu.board;

import com.hansol.tofu.board.domain.dto.BoardCreationRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardEditRequestDTO;
import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.board.repository.BoardRepository;
import com.hansol.tofu.clubphoto.ClubPhotoService;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.mock.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class BoardServiceTest {

    private BoardService sut;
    private BoardRepository boardRepository;
    private MemberRepository memberRepository;
    private ClubPhotoService clubPhotoService;

    @BeforeEach
    void setUp() {
        boardRepository = mock(BoardRepository.class);
        memberRepository = mock(MemberRepository.class);
        clubPhotoService = mock(ClubPhotoService.class);
        sut = new BoardService(boardRepository, memberRepository, clubPhotoService);
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void addBoard_동호회_게시글_작성에_성공한다() throws Exception {
        Long memberId = 1L;
        Long memberAsClubId = 3L;
		List<MultipartFile> mockMultipartFiles = List.of(new MockMultipartFile("t1", "t1.jpg", "image/jpeg", "t1".getBytes()));
        var boardCreationRequestDTO = BoardCreationRequestDTO.builder().build();
        var memberEntity = MemberEntity.builder().build();
        var boardEntity = boardCreationRequestDTO.toEntity(memberEntity, memberAsClubId);
        when(memberRepository.findMemberByIdAndMemberStatus(memberId, MemberStatus.ACTIVATE))
                .thenReturn(Optional.of(memberEntity));
        when(boardRepository.save(boardEntity)).thenReturn(boardEntity);


        sut.addBoard(memberAsClubId, boardCreationRequestDTO, mockMultipartFiles);


        verify(boardRepository, times(1)).save(boardEntity);
    }

    // @Test
	// @Disabled
    // @WithMockCustomUser(username = "lisa@test.com")
    // void editBoard_동호회_게시글_변경에_성공한다() throws Exception {
    //     Long memberId = 1L;
    //     Long boardId = 22L;
    //     Long memberAsClubId = 3L;
    //     var memberEntity = MemberEntity.builder().id(memberId).build();
    //     var boardCreationRequestDTO = BoardEditRequestDTO.builder()
    //             .title("변경된 제목")
    //             .content("변경된 내용")
    //             .build();
    //     var boardEntity = BoardEntity.builder()
    //             .id(boardId)
    //             .title("제목")
    //             .content("내용")
    //             .member(memberEntity)
    //             .build();
    //     when(boardRepository.findById(boardEntity.getId())).thenReturn(Optional.of(boardEntity));
	//
	//
    //     sut.editBoard(memberAsClubId, boardId, boardCreationRequestDTO);
	//
	//
    //     assertEquals(boardCreationRequestDTO.title(), boardEntity.getTitle());
    //     assertEquals(boardCreationRequestDTO.content(), boardEntity.getContent());
    // }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editBoard_동호회_게시글_삭제에_성공한다() throws Exception {
        Long memberId = 1L;
        Long boardId = 22L;
        Long memberAsClubId = 3L;
        var memberEntity = MemberEntity.builder().id(memberId).build();
        var boardEntity = BoardEntity.builder()
                .id(boardId)
                .title("제목")
                .content("내용")
                .member(memberEntity)
                .build();
        when(boardRepository.findById(boardEntity.getId())).thenReturn(Optional.of(boardEntity));


        sut.deleteBoard(memberAsClubId, boardId);


        assertEquals(BoardStatus.DELETED, boardEntity.getBoardStatus());
    }

}
