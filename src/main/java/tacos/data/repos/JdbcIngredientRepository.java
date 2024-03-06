package tacos.data.repos;

import tacos.data.Ingredient;

import java.util.Optional;

public interface JdbcIngredientRepository {

    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);

}
