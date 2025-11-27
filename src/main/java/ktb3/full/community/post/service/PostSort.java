package ktb3.full.community.post.service;

import ktb3.full.community.post.domain.Post;

import java.util.Arrays;
import java.util.Comparator;


public enum PostSort {
    RECENT(Comparator.comparing(Post::getCreatedAt).reversed()),
    OLDEST(Comparator.comparing(Post::getCreatedAt)),
    VIEWS(Comparator.comparing(Post::getViewCount).reversed());
    //LIKES(Comparator.comparing(Post::getLikes).reversed());

    public final Comparator<Post> comparator;

    PostSort(Comparator<Post> comparator) {
        this.comparator = comparator;
    }

    public static PostSort of(String key) {
        if (key == null || key.isEmpty()) return RECENT;

        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(key))
                .findFirst()
                .orElse(RECENT);
    }
}
