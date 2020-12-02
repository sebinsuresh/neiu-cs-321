package dboard.data;

import dboard.DoodlePost;
import dboard.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoodlePostRepository extends CrudRepository<DoodlePost, Long> {

    List<DoodlePost> findAllByUserOrderByPostedAtDesc(User user, Pageable pageable);
    List<DoodlePost> findAll(Pageable pageable);

    Long countByUser(User user);
}
