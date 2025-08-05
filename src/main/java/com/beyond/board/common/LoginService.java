package com.beyond.board.common;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 세션 로그인 흐름
// 1. 화면에서 doLogin 메서드 호출
// 2. 스프링에서 사전에 구현된 doLogin 메서드에서 loadUserByUsername 메서드 실행
// 3. loadUserByUsername에서 리턴 받는 User 객체를 사용하여 사용자가 입력한 email/password와 비교
// 4. 검증이 완료되면 DB의 user 객체를 Authentication 객체로 저장 및 세션 ID 발급
// 5. 사용자는 세션 ID를 발급받고 서버는 세션ID를 저장하여 API 요청할 때 마다 세션 ID를 검증

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final AuthorRepository authorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("회원정보가 존재하지 않습니다."));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + author.getRole()));
        return new User(author.getEmail(), author.getPassword(), authorities);
    }
}
