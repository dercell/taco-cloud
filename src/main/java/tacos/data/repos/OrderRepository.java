package tacos.data.repos;

import org.springframework.data.repository.CrudRepository;
import tacos.data.TacoOrder;


public interface OrderRepository extends CrudRepository<TacoOrder, String> {
}
