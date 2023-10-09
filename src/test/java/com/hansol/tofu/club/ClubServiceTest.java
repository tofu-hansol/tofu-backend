package com.hansol.tofu.club;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import com.hansol.tofu.auth.domain.model.CustomUserDetails;
import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.category.repository.CategoryRepository;
import com.hansol.tofu.club.domain.dto.ClubCreationRequestDTO;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.domain.entity.ClubMemberEntity;
import com.hansol.tofu.club.enums.ClubJoinStatus;
import com.hansol.tofu.club.repository.ClubMemberRepository;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.repository.MemberRepository;
import com.hansol.tofu.upload.image.StorageService;

class ClubServiceTest {

	private ClubService sut;
	private ClubRepository clubRepository;
	private CategoryRepository categoryRepository;
	private ClubMemberRepository clubMemberRepository;
	private StorageService storageService;
	private MemberRepository memberRepository;

	@BeforeAll
	static void setUpAll() {
		UserDetails userDetails = new CustomUserDetails("lisa@test.com", "test1234", 1L, Collections.emptyList(), Collections.emptyMap());
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
		clubMemberRepository = mock(ClubMemberRepository.class);
		categoryRepository = mock(CategoryRepository.class);
		clubRepository = mock(ClubRepository.class);
		storageService = mock(StorageService.class);
		memberRepository = mock(MemberRepository.class);
		sut = new ClubService(clubRepository, categoryRepository, storageService, clubMemberRepository, memberRepository);
	}

	@Test
	void addClub_동호회_생성에_성공한다() throws Exception {
		var categoryEntity = CategoryEntity.builder().name("운동").build();
		var clubRequestDTO = ClubCreationRequestDTO.builder()
			.name("tofu-club")
			.description("tofu club")
			.accountNumber("123-456-789")
			.fee(10000)
			.categoryId(1L)
			.build();
		var clubEntity = clubRequestDTO.toEntity(categoryEntity);
		when(categoryRepository.findById(clubRequestDTO.categoryId())).thenReturn(Optional.of(categoryEntity));
		when(clubRepository.save(clubEntity)).thenReturn(clubEntity);


		sut.addClub(clubRequestDTO);


		verify(clubRepository).save(Mockito.any(ClubEntity.class));
		verify(categoryRepository).findById(clubRequestDTO.categoryId());
	}

	@Test
	void editClub_동호회_정보변경_요청시_성공한다() throws Exception {
		Long clubId = 1L;
		var categoryEntity = CategoryEntity.builder().name("운동").build();
		var clubEditRequestDTO = ClubEditRequestDTO.builder()
			.description("동호회설명변경")
			.accountNumber("222-2222-2222")
			.categoryId(2L)
			.fee(200000)
			.build();
		var clubEntity = ClubEntity.builder()
			.id(clubId)
			.name("tofu-club")
			.description("tofu club")
			.accountNumber("123-456-789")
			.fee(10000)
			.category(CategoryEntity.builder().name("요리").build())
			.build();

		when(categoryRepository.findById(clubEditRequestDTO.categoryId())).thenReturn(Optional.of(categoryEntity));
		when(clubRepository.findById(clubId)).thenReturn(Optional.of(clubEntity));


		sut.editClub(clubId, clubEditRequestDTO);


		assertEquals(clubEntity.getDescription(), clubEditRequestDTO.description());
		assertEquals(clubEntity.getAccountNumber(), clubEditRequestDTO.accountNumber());
		assertEquals(clubEntity.getCategory().getName(), categoryEntity.getName());
		assertEquals(clubEntity.getFee(), clubEditRequestDTO.fee());
	}

	@Test
	void changeBackgroundImage_동호회_배경사진_변경에_성공한다() {
		MockMultipartFile backgroundImage = new MockMultipartFile("backgroundImage", "back.jpg", "image/jpeg", "test".getBytes());
		var clubEntity = ClubEntity.builder().id(2L).build();

		when(clubRepository.findById(2L)).thenReturn(Optional.of(clubEntity));
		when(storageService.uploadImage(backgroundImage, "images/club/")).thenReturn("http://image.com/backgroundImage");


		sut.changeBackgroundImage(clubEntity.getId(), backgroundImage);


		assertEquals(clubEntity.getBackgroundUrl(), "http://image.com/backgroundImage");
		verify(storageService).uploadImage(backgroundImage, "images/club/");
	}

	@Test
	void changeProfileImage_동호회_프로필사진_변경에_성공한다() {
		MockMultipartFile profileImage = new MockMultipartFile("profileImage", "club_profile.jpg", "image/jpeg", "test".getBytes());
		var clubEntity = ClubEntity.builder().id(2L).build();

		when(clubRepository.findById(2L)).thenReturn(Optional.of(clubEntity));
		when(storageService.uploadImage(profileImage, "images/club/")).thenReturn("http://image.com/profileImage");


		sut.changeProfileImage(clubEntity.getId(), profileImage);


		assertEquals(clubEntity.getProfileUrl(), "http://image.com/profileImage");
		verify(storageService).uploadImage(profileImage, "images/club/");
	}

	@Test
	@WithUserDetails(
		value = "lisa@test.com",
		setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void requestJoinClub_동호회가_존재하는_경우_가입신청이_완료된다() {
		var clubId = 1L;
		var clubEntity = ClubEntity.builder().id(clubId).build();
		var memberEntity = MemberEntity.builder().id(1L).build();
		var clubMemberEntity = ClubMemberEntity.builder().club(clubEntity).member(memberEntity).build();
		when(clubRepository.findById(clubId)).thenReturn(Optional.of(clubEntity));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(memberEntity));
		when(clubMemberRepository.save(clubMemberEntity)).thenReturn(
			ClubMemberEntity.builder().id(2L).club(clubEntity).member(memberEntity).build()
		);


		Long clubMemberEntityId = sut.requestJoinClub(clubId);


		verify(clubMemberRepository, times(1)).save(clubMemberEntity);
		assertEquals(clubMemberEntityId, 2L);
	}

	@Test
	@WithUserDetails(
		value = "lisa@test.com",
		setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void cancelJoinClub_동호회가_존재하는_경우_가입신청_취소시_취소에_성공한다() {
		var clubId = 1L;
		var clubMemberEntity = ClubMemberEntity.builder().id(2L).club(ClubEntity.builder().id(clubId).build()).member(MemberEntity.builder().id(3L).build()).build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubId, 1L)).thenReturn(Optional.of(clubMemberEntity));


		Long clubMemberEntityId = sut.cancelJoinClub(clubId);


		verify(clubMemberRepository, times(1)).deleteById(clubMemberEntity.getId());
		assertEquals(clubMemberEntityId, 2L);
	}

	@Test
	@WithUserDetails(
		value = "mchi@test.com",
		setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void approveClubJoinRequest_동호회원_가입요청_승인에_성공한다() {
		var clubMemberId = 1L;
		var clubEntity = ClubEntity.builder().id(1L).build();
		var memberEntity = MemberEntity.builder().id(3L).build();
		var clubMemberEntity = ClubMemberEntity.builder()
			.id(2L)
			.club(clubEntity)
			.member(memberEntity)
			.build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubEntity.getId(), memberEntity.getId()))
			.thenReturn(Optional.of(clubMemberEntity));


		sut.acceptJoinClub(clubEntity.getId(), memberEntity.getId());


		assertEquals(clubMemberEntity.getClubJoinStatus(), ClubJoinStatus.ACCEPTED);
	}

	@Test
	@WithUserDetails(
		value = "mchi@test.com",
		setupBefore = TestExecutionEvent.TEST_EXECUTION
	)
	void rejectClubJoinRequest_동호회원_가입요청_거절에_성공한다() {
		var clubMemberId = 1L;
		var clubEntity = ClubEntity.builder().id(1L).build();
		var memberEntity = MemberEntity.builder().id(3L).build();
		var clubMemberEntity = ClubMemberEntity.builder()
			.id(2L)
			.club(clubEntity)
			.member(memberEntity)
			.build();
		when(clubMemberRepository.findByClubIdAndMemberId(clubEntity.getId(), memberEntity.getId()))
			.thenReturn(Optional.of(clubMemberEntity));


		sut.rejectJoinClub(clubEntity.getId(), memberEntity.getId());


		assertEquals(clubMemberEntity.getClubJoinStatus(), ClubJoinStatus.REJECTED);
	}
}
