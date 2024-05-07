REST-сервис на Spring Framework (Spring Core, Spring Context, Spring Bean, Spring Web MVC, Spring Data JPA (Hibernate)) для кулинарный рецептов. Дополнительно использовались HikariCP, Mapstruckt, Jackson. 
Для тестов использовались Junit, Mockito, Spring Test , дополнительно для тестирования репозитория БД использовался testcontainers.
Демонстрация работы: https://youtu.be/-ifMpSf-gLs

Схема БД (PostgreSQL 15):

![image](https://github.com/Kipokape/cookingRecipesRest/assets/57583013/a2348943-700d-42a9-b650-cb96efc22236)

Реализованы CRUD для всех сущностей.

Category:

 - GET http://localhost:8088/api/category/{id} - Запрос для получения данных категории;
 - GET http://localhost:8088/api/category - Запрос для получения данных всех категорий;
 - POST http://localhost:8088/api/category - Запрос для добавления новой категории (необходимо "name");
 - PATCH http://localhost:8088/api/category - Запрос для изменения категории (необходимо "categoryId", "name");
 - DELETE http://localhost:8088/api/category/{id} - Запрос для удаления категории;


Recipe:

 - GET http://localhost:8088/api/recipe/{id} - Запрос для получения данных рецепта;
 - GET http://localhost:8088/api/recipe - Запрос для получения данных всех рецептов;
 - POST http://localhost:8088/api/recipe - Запрос для добавления нового рецепта (необходимо "categoryId", "name");
 - PATCH http://localhost:8088/api/recipe - Запрос для изменения рецепта (необходимо "recipeId", "categoryId", "name");
 - DELETE http://localhost:8088/api/recipe/{id} - Запрос для удаления рецепта;


Ingredient:

 - GET http://localhost:8088/api/ingredient/{id} - Запрос для получения данных ингредиента;
 - GET http://localhost:8088/api/ingredient - Запрос для получения данных всех ингредиентов;
 - POST http://localhost:8088/api/ingredient - Запрос для добавления нового ингредиента (необходимо "name");
 - PATCH http://localhost:8088/api/ingredient - Запрос для изменения ингредиента (необходимо "ingredientId", "name");
 - DELETE http://localhost:8088/api/ingredient/{id} - Запрос для удаления ингредиента;


RecipeIngredient:

 - GET http://localhost:8088/api/recipe-ingredient/{id} - Запрос для получения данных ингредиента рецепта;
 - GET http://localhost:8088/api/recipe-ingredient - Запрос для получения данных всех ингредиентов рецептов;
 - POST http://localhost:8088/api/recipe-ingredient - Запрос для добавления нового ингредиента рецепта (необходимо "recipeId", "ingredientId", "weight");
 - PATCH http://localhost:8088/api/recipe-ingredient - Запрос для изменения ингредиента рецепта (необходимо "recipeIngredientsId", "recipeId", "ingredientId", "weight");
 - DELETE http://localhost:8088/api/recipe-ingredient/{id} - Запрос для удаления ингредиента рецепта;
