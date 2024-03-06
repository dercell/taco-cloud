package tacos.data.repos;

import tacos.data.TacoOrder;

public interface JdbcOrderRepository {

    TacoOrder save(TacoOrder tacoOrder);

}
