package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static Comment toComment(CommentInDto commentInDto, Item item, User user) {
        return new Comment(commentInDto.getText(), item.getId(), user.getId(), user.getName());
    }

    public static CommentOutDto toDto(Comment comment) {
        return new CommentOutDto(comment.getId(), comment.getText(), comment.getAuthorName(), comment.getCreated());
    }

    public static ItemBookingDto.Comment toItemComment(Comment comment) {
        return new ItemBookingDto.Comment(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName(),
                comment.getCreated());
    }
}
