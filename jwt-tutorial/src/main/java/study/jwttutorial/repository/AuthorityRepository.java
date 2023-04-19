package study.jwttutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jwttutorial.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
