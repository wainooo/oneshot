package ezen.oneshot.service;

import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Membership login(String loginId, String password) {
        Optional<Membership> findMemberOptional = memberRepository.findByLoginId(loginId);
        if (findMemberOptional.isPresent()) {
            Membership membership = findMemberOptional.get();
            if (membership.getPassword().equals(password)) {
                return membership;
            }
            else {
                return null;
            }
        }
        return null;
    }
}
