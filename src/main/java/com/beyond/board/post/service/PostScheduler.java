package com.beyond.board.post.service;

import com.beyond.board.post.domain.Post;
import com.beyond.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostScheduler {

    private final PostRepository postRepository;
    @Scheduled(cron = "0 0/1 * * * *")
    public void postScheduler() {
        log.info("===== 예약 스케쥴러 시작 ======");
        List<Post> posts =postRepository.findByAppointment("Y");
        LocalDateTime now = LocalDateTime.now();
        for (Post post : posts) {
            if(post.getAppointmentTime().isBefore(now)) {
                post.updateAppointment("N");
            }
        }
        log.info("===== 예약 스케쥴러 끝 ======");
    }
}
