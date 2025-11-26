package ktb3.full.community.Comment.service;

import ktb3.full.community.Comment.domain.Comment;

import java.util.Arrays;
import java.util.Comparator;

public enum CommentSort {
    RECENT(Comparator.comparing(Comment::getCreatedAt).reversed()),
    OLDEST(Comparator.comparing(Comment::getCreatedAt));

    public final Comparator<Comment> comparator;

    CommentSort(Comparator<Comment> comparator) {
        this.comparator = comparator;
    }

    public static CommentSort of (String key) {
        if (key == null || key.isEmpty()) return RECENT;

        return Arrays.stream(CommentSort.values())
                .filter(value -> value.name().equalsIgnoreCase(key))
                .findFirst()
                .orElse(RECENT);
    }
}
