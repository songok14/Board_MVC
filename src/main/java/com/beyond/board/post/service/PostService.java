package com.beyond.board.post.service;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.post.domain.Post;
import com.beyond.board.post.dto.PostCreateDto;
import com.beyond.board.post.dto.PostDetailDto;
import com.beyond.board.post.dto.PostListDto;
import com.beyond.board.post.repository.PostRepository;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto postCreateDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        Author author = authorRepository.findByEmail("admin@naver.com").orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        LocalDateTime appointmentTime = null;
        if(postCreateDto.getAppointment().equals("Y")){
            if (postCreateDto.getAppointmentTime()==null || postCreateDto.getAppointmentTime().isEmpty()){
                throw new IllegalArgumentException("시간 정보가 비어져 있습니다");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(postCreateDto.getAppointmentTime(), dateTimeFormatter);
        }
        postRepository.save(postCreateDto.toEntity(author, appointmentTime));
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 Id 입니다."));
        return PostDetailDto.fromEntity(post);

    }

    public List<PostListDto> findAll() {
        List<Post> postList = postRepository.findAll();
        return postList.stream().map(a -> PostListDto.postListDtoFromEntity(a)).collect(Collectors.toList());
    }

    public Page<PostListDto> findAll(Pageable pageable) {
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(a -> PostListDto.postListDtoFromEntity(a));
    }
}