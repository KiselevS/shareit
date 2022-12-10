package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdOrderByEndDesc(Long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerCurrentBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end < :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerPastBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.end > :currentDate " +
            "order by b.end desc")
    List<Booking> findByBookerFutureBookings(@Param("bookerId") Long bookerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    List<Booking> findByBooker_IdAndStatusOrderByEndDesc(Long bookerId, Status status, Pageable pageable);

    List<Booking> findByItem_OwnerOrderByEndDesc(Long id, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = :ownerId " +
            "and b.start <= :currentDate " +
            "and b.end >= :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdCurrentBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = :ownerId " +
            "and b.end < :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdPastBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.item.owner = :ownerId " +
            "and b.end > :currentDate " +
            "order by b.end desc")
    List<Booking> findByOwnerIdFutureBookings(@Param("ownerId") Long ownerId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    List<Booking> findByItem_OwnerAndStatusOrderByEndDesc(Long ownerId, Status status, Pageable pageable);

    List<Booking> findByItem_IdAndItem_Owner(long itemId, long userId);

    List<Booking> findByBooker_Id(long bookingId);

    List<Booking> findByItem_Id(long id);
}
