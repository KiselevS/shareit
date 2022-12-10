package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_Id(long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM requests " +
            "LEFT JOIN items i on requests.id = i.request_id " +
            "WHERE owner_id = :ownerId")
    List<ItemRequest> findAllByOwner_Id(long ownerId, Pageable pageable);
}
