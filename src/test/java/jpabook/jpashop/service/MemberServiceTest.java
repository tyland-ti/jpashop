package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void joinTest() throws Exception {
        //given
        Member member = new Member();
        member.setName("테스트1");
        //when
        Long id = memberService.join(member);

        entityManager.flush();
        //then
        assertEquals(member, memberService.findOne(id));
        assertEquals(id, member.getId());

    }

    @Test
    @DisplayName("중복회원 가입테스트")
    public void duplicateMemberTest() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("테스트");

        Member member2 = new Member();
        member2.setName("테스트");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}