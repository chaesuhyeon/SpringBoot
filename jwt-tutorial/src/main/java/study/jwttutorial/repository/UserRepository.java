package study.jwttutorial.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import study.jwttutorial.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities") //Eager 조회로 authorities 정보 가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
