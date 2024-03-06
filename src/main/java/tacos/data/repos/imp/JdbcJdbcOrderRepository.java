package tacos.data.repos.imp;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.data.Ingredient;
import tacos.data.Taco;
import tacos.data.TacoOrder;

import java.util.Arrays;
import java.util.Date;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcJdbcOrderRepository implements tacos.data.repos.JdbcOrderRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcJdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory("insert into Taco_Order " +
                        "(delivery_name, delivery_street, delivery_city, " +
                        "delivery_state, delivery_zip, cc_number, " +
                        "cc_expiration, cc_cvv, placed_at) " +
                        "values(?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);

        pscf.setReturnGeneratedKeys(true);

        tacoOrder.setPlacedAt(new Date());

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tacoOrder.getDeliveryName(),
                        tacoOrder.getDeliveryStreet(),
                        tacoOrder.getDeliveryCity(),
                        tacoOrder.getDeliveryState(),
                        tacoOrder.getDeliveryZip(),
                        tacoOrder.getCcNumber(),
                        tacoOrder.getCcExpiration(),
                        tacoOrder.getCcCVV(),
                        tacoOrder.getPlacedAt()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);

        List<Taco> tacos = tacoOrder.getTacos();
        int i=0;
        for(Taco taco : tacos){
            saveTaco(orderId, i++, taco);
        }

        return tacoOrder;
    }

    private long saveTaco(long orderId, int orderKey, Taco taco){
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Taco " +
                                "(name, created_at, taco_order, taco_order_key)" +
                                "values (?,?,?,?)",
                        Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
                );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatedAt(),
                        orderId,
                        orderKey
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<Ingredient> ingredients){
        int key = 0;
        for(Ingredient ingredient : ingredients){
            jdbcTemplate.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key)" +
                            "values (?,?,?)",
                    ingredient.getId(), tacoId, key++
            );
        }
    }
}