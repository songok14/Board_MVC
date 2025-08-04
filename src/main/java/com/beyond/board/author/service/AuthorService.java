package com.beyond.board.author.service;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.dto.*;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(AuthorCreateDto authorCreateDto) {
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (authorCreateDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
        }
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);
        authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        return authorRepository.findAll().stream().map(a -> a.authorListDtoFromEntity()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));

        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto myinfo() throws NoSuchElementException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));
        return AuthorDetailDto.fromEntity(author);
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDTO) {
        Author author = authorRepository.findByEmail(authorUpdatePwDTO.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));
        author.updatePw(authorUpdatePwDTO.getPassword());
        System.out.println(author.getPassword());
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(author);
    }

    public Author login(AuthorLoginDto authorLoginDto){
        Optional<Author> optionalAuthor = authorRepository.findByEmail(authorLoginDto.getEmail());
        boolean check= true;
        if(!optionalAuthor.isPresent()){
            check=false;
        }else{
            if(!passwordEncoder.matches(authorLoginDto.getPassword(), optionalAuthor.get().getPassword())){
                check=false;
            }
        }
        if(!check) {
            System.out.println("로그인 실패");
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalAuthor.get();
    }
}