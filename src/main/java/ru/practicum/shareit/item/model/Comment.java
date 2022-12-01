package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text_author")
    private String text;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "author_id")
    private long authorId;

    @Length(max = 32)
    @Column(name = "author_name")
    private String authorName;

    @Column(name = "created")
    private LocalDateTime created;

    public Comment(String text, long itemId, long authorId, String authorName) {
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.created = LocalDateTime.now();
    }
}
