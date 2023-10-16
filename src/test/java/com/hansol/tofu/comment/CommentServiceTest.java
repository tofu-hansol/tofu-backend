package com.hansol.tofu.comment;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.board.repository.BoardRepository;
import com.hansol.tofu.comment.domain.dto.CommentCreationRequestDTO;
import com.hansol.tofu.comment.domain.dto.CommentEditRequestDTO;
import com.hansol.tofu.comment.domain.entity.CommentEntity;
import com.hansol.tofu.comment.enums.CommentStatus;
import com.hansol.tofu.comment.repository.CommentRepository;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.mock.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class CommentServiceTest {

    private CommentService sut;
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        memberRepository = mock(MemberRepository.class);
        boardRepository = mock(BoardRepository.class);
        sut = new CommentService(commentRepository, boardRepository, memberRepository);
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void addComment_동호회_댓글_작성에_성공한다() throws Exception {
        Long memberId = 1L;
        Long boardId = 2L;
        var commentCreationRequestDTO = CommentCreationRequestDTO.builder().build();
        var memberEntity = MemberEntity.builder().build();
        var boardEntity = BoardEntity.builder().id(boardId).member(memberEntity).build();
        var commentEntity = CommentEntity.builder().id(3L).build();

        when(memberRepository.findByIdAndMemberStatus(memberId, MemberStatus.ACTIVATE))
                .thenReturn(Optional.of(memberEntity));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardEntity));
        when(commentRepository.save(commentCreationRequestDTO.toEntity(memberEntity, boardEntity))).thenReturn(commentEntity);


        sut.addComment(boardId, commentCreationRequestDTO);


        verify(commentRepository, times(1)).save(commentCreationRequestDTO.toEntity(memberEntity, boardEntity));
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editComment_동호회_댓글_변경에_성공한다() throws Exception {
        Long memberId = 1L;
        Long boardId = 2L;
        Long commentId = 3L;
        var commentEditRequestDTO = CommentEditRequestDTO.builder().content("변경후 댓글").build();
        var memberEntity = MemberEntity.builder().id(memberId).build();
        var commentEntity = CommentEntity.builder().id(commentId).member(memberEntity).content("변경전 댓글").build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));


        sut.editComment(boardId, commentId, commentEditRequestDTO);


        assertEquals(commentEditRequestDTO.content(), commentEntity.getContent());
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void deleteComment_동호회_댓글_삭제에_성공한다() throws Exception {
        Long memberId = 1L;
        Long boardId = 2L;
        Long commentId = 3L;
        var memberEntity = MemberEntity.builder().id(memberId).build();
        var commentEntity = CommentEntity.builder().id(commentId).member(memberEntity).content("변경전 댓글").build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));


        sut.deleteComment(boardId, commentId);


        assertEquals(CommentStatus.DELETED, commentEntity.getCommentStatus());
    }


}