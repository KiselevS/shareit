package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookingInDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<DateConstraint, BookingInDto> {
    @Override
    public boolean isValid(BookingInDto bookingInDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();

        return (bookingInDto.getStart().isAfter(currentDate) || bookingInDto.getStart().isEqual(currentDate))
                && (bookingInDto.getEnd().isAfter(currentDate) || bookingInDto.getEnd().isEqual(currentDate))
                && (bookingInDto.getStart().isBefore(bookingInDto.getEnd()) || bookingInDto.getEnd().isEqual(currentDate));
    }
}
