package tacos.data.repos;

import org.springframework.data.repository.CrudRepository;
import tacos.data.User;

public interface JpaUserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
