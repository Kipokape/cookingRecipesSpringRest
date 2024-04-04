CREATE TABLE IF NOT EXISTS category
(
    id_category   BIGSERIAL PRIMARY KEY,
    name_category VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS recipe
(
    id_recipe   BIGSERIAL PRIMARY KEY,
    name_recipe VARCHAR(100) NOT NULL,
    id_category BIGINT       NOT NULL,
    FOREIGN KEY (id_category) REFERENCES category ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ingredient
(
    id_ingredient   BIGSERIAL PRIMARY KEY,
    name_ingredient VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS recipe_ingredients
(
    id_recipe_ingredients BIGSERIAL PRIMARY KEY,
    id_ingredient         BIGINT  NOT NULL,
    id_recipe             BIGINT  NOT NULL,
    weight                INTEGER NOT NULL,
    FOREIGN KEY (id_ingredient) REFERENCES ingredient ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_recipe) REFERENCES recipe ON DELETE SET NULL ON UPDATE CASCADE
);

TRUNCATE TABLE category CASCADE;
TRUNCATE TABLE recipe CASCADE;
TRUNCATE TABLE ingredient CASCADE;
TRUNCATE TABLE recipe_ingredients CASCADE;


INSERT INTO category(id_category, name_category)
VALUES (1, 'Первые блюда'),
       (2, 'Вторые блюда'),
       (3, 'Дессерты');

INSERT INTO recipe(id_recipe, name_recipe, id_category)
VALUES (1, 'Борщ', 1),
       (2, 'Щи', 1),
       (3, 'Омлет', 2),
       (4, 'Рагу', 2),
       (5, 'Вафли', 3);

INSERT INTO ingredient(id_ingredient, name_ingredient)
VALUES (1, 'Капуста'),
       (2, 'Картошка'),
       (3, 'Вода'),
       (4, 'Мука'),
       (5, 'Яйца'),
       (6, 'Мёд'),
       (7, 'Свекла');

INSERT INTO recipe_ingredients(id_recipe_ingredients, id_ingredient, id_recipe, weight)
VALUES (1, 1, 1, 300),
       (2, 2, 1, 100),
       (3, 3, 1, 500),
       (4, 4, 1, 200),
       (5, 1, 2, 300),
       (6, 2, 2, 100),
       (7, 3, 2, 500),
       (8, 5, 3, 500),
       (9, 2, 4, 500),
       (10, 3, 4, 100),
       (11, 3, 5, 200),
       (12, 4, 5, 300),
       (13, 5, 5, 100);

