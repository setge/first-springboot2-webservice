package com.setge.boot.springboot.web.service.posts;

import com.setge.boot.springboot.web.domain.posts.Posts;
import com.setge.boot.springboot.web.domain.posts.PostsRepository;
import com.setge.boot.springboot.web.web.dto.PostsListResponseDto;
import com.setge.boot.springboot.web.web.dto.PostsResponseDto;
import com.setge.boot.springboot.web.web.dto.PostsSaveRequestDto;
import com.setge.boot.springboot.web.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // final이 붙은 필드를 인자값으로하는 생성자를 만들어준다.
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly=true) // 조회 속도가 개선된다. 조화 기능 메서드라면 적극 사용
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts); // JpaRepository는 delete 메서드를 지원한다.
    }

}
