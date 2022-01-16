package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong count = new AtomicLong();
    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id)&& !posts.get(id).isRemoved()) return Optional.of(posts.get(id));
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(count.incrementAndGet());
            posts.put(post.getId(), post);
        } else if (post.getId() != 0) {
            if (posts.containsKey(post.getId()) && !posts.get(post.getId()).isRemoved()) {
                posts.replace(post.getId(), post);
            } else {
                throw new NotFoundException();
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id) && !posts.get(id).isRemoved()) {
            posts.get(id).setRemoved(true);
        } else throw new NotFoundException();
    }
}
