package online.jeweljoust.BE.respository;

import online.jeweljoust.BE.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ResourceRepository extends JpaRepository<Resources, Long> {
}
