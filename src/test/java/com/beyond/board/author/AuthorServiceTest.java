package com.beyond.board.author;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.dto.AuthorCreateDto;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.author.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest() {
        // given
        AuthorCreateDto authorCreateDto = AuthorCreateDto.builder()
                .name("abc")
                .email("abc@naver.com")
                .password("12341234")
                .build();

        // when
        authorService.save(authorCreateDto);

        // then
        Author authorDb = authorRepository.findByEmail("abc@naver.com").orElse(null);
        Assertions.assertEquals(authorCreateDto.getEmail(), authorDb.getEmail());

    }

    @Test
    public void findAllTest() {
        // findAll을 통한 결과값의 개수가 맞는지 검증

        // 최초 갯수
        long startCount = authorService.findAll().size();

        // 데이터 추가
        authorRepository.save(Author.builder()
                .name("abc1")
                .email("abc1@naver.com")
                .password("12341234")
                .build());

        authorRepository.save(Author.builder()
                .name("abc2")
                .email("abc2@naver.com")
                .password("12341234")
                .build());

        authorRepository.save(Author.builder()
                .name("abc3")
                .email("abc3@naver.com")
                .password("12341234")
                .build());

        // 마지막 갯수 = 최초 갯수 + 추가 갯수
        long endCount = authorService.findAll().size();
        Assertions.assertEquals(startCount + 3, endCount);
    }
}
