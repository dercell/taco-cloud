package tacos.data.repos;

import org.springframework.data.repository.CrudRepository;
import tacos.data.TacoOrder;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID> {
}
