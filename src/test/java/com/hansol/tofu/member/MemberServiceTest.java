package com.hansol.tofu.member;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.dept.domain.DeptEntity;
import com.hansol.tofu.dept.repository.DeptRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.domain.dto.MemberEditRequestDTO;
import com.hansol.tofu.member.domain.dto.MemberJoinRequestDTO;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.upload.image.StorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;
import java.util.Optional;

import static com.hansol.tofu.error.ErrorCode.DUPLICATE_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberService sut;

    private MemberRepository memberRepository;
    private DeptRepository deptRepository;
	private StorageService storageService;

    @BeforeAll
    static void setUpAll() {
        UserDetails userDetails = new CustomUserDetails("test@test.com", "test1234", 1L, Collections.emptyList(), Collections.emptyMap());
        var authentication = new UsernamePasswordAuthenticationToken(
      			userDetails,
      			null,
      			userDetails.getAuthorities()
      		);

      		var context = SecurityContextHolder.createEmptyContext();
      		context.setAuthentication(authentication);
      		SecurityContextHolder.setContext(context);
    }

    @BeforeEach
    void setUp() {
        deptRepository = mock(DeptRepository.class);
        memberRepository = mock(MemberRepository.class);
		storageService = mock(StorageService.class);
        sut = new MemberService(memberRepository, deptRepository, storageService);
    }

    @Test
    void saveMember_회원_저장_요청시_저장에_성공한다() {
        var memberRequestDTO = MemberJoinRequestDTO.builder()
                .email("test@hansol.com")
                .password("test1234")
                .name("뭉치")
                .deptId(1L)
                .mbti("ENFJ")
                .build();
        var deptEntity = DeptEntity.builder().build();
        var memberEntity = memberRequestDTO.toEntity(memberRequestDTO, deptEntity);
        when(deptRepository.findById(memberRequestDTO.deptId())).thenReturn(Optional.of(deptEntity));
        when(memberRepository.existsMemberByEmail(memberRequestDTO.email())).thenReturn(false);
        when(memberRepository.save(memberEntity)).thenReturn(memberEntity);


        sut.saveMember(memberRequestDTO);


        verify(memberRepository).save(memberEntity);
    }


    @Test
    void saveMember_존재하는_이메일로_회원가입요청시_회원저장에_실패한다() {
        var memberRequestDTO = MemberJoinRequestDTO.builder()
                .email("test@hansol.com")
                .password("test1234")
                .name("뭉치")
                .deptId(1L)
                .mbti("ENFJ")
                .build();
        var deptEntity = DeptEntity.builder().build();
        when(deptRepository.findById(memberRequestDTO.deptId())).thenReturn(Optional.of(deptEntity));
        when(memberRepository.existsMemberByEmail(memberRequestDTO.email())).thenReturn(true);


        var baseException = assertThrows(BaseException.class, () -> sut.saveMember(memberRequestDTO));


        assertEquals(DUPLICATE_MEMBER.getMessage(), baseException.getMessage());
    }

    // TODO: https://skyriv312079.tistory.com/179
	@Test
    @WithUserDetails(
            value = "test@test.com",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
	void editMember_유효하는_정보로_회원정보_수정에_성공한다() {
		var memberEditRequestDTO = MemberEditRequestDTO.builder()
			.name("뭉치")
			.companyId(3L)
			.deptId(5L)
			.position("책임")
			.mbti("ENFJ")
			.build();
        var memberEntity = MemberEntity.builder().build();
        var deptEntity = DeptEntity.builder().id(memberEditRequestDTO.deptId()).build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
        when(deptRepository.findById(memberEditRequestDTO.deptId())).thenReturn(Optional.of(deptEntity));


        sut.editMemberProfile(memberEditRequestDTO);


        assertEquals(memberEntity.getName(), memberEditRequestDTO.name());
        assertEquals(memberEntity.getDept().getId(), memberEditRequestDTO.deptId());
        assertEquals(memberEntity.getPosition(), memberEditRequestDTO.position());
        assertEquals(memberEntity.getMbti(), memberEditRequestDTO.mbti());
	}

    @Test
    @WithUserDetails(
              value = "test@test.com",
              setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
	void changeMemberProfileImage_유효하는_정보로_회원정보_수정에_성공한다() {
		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "test".getBytes());
        var memberEntity = MemberEntity.builder().build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
        when(storageService.uploadImage(profileImage, "images/member/")).thenReturn("http://image.com/testImage");


		sut.changeMemberProfileImage(profileImage);


        assertEquals(memberEntity.getProfileUrl(), "http://image.com/testImage");
        verify(storageService).uploadImage(profileImage, "images/member/");
	}


}
