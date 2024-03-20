package tacos.data.repos;

import org.springframework.data.repository.CrudRepository;
import tacos.data.Ingredient;

public interface JpaIngredientRepository extends CrudRepository<Ingredient, String> {
}
