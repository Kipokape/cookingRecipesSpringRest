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


INSERT INTO category(name_category)
VALUES ('Первые блюда'),
       ('Вторые блюда'),
       ('Десерты');

INSERT INTO recipe(name_recipe, id_category)
VALUES ('Борщ', 1),
       ('Щи', 1),
       ('Омлет', 2),
       ('Рагу', 2),
       ('Вафли', 3);

INSERT INTO ingredient(name_ingredient)
VALUES ('Капуста'),
       ('Картошка'),
       ('Вода'),
       ('Мука'),
       ('Яйца'),
       ('Мёд'),
       ('Свекла');

INSERT INTO recipe_ingredients(id_ingredient, id_recipe, weight)
VALUES (1, 1, 300),
       (2, 1, 100),
       (3, 1, 500),
       (4, 1, 200),
       (1, 2, 300),
       (2, 2, 100),
       (3, 2, 500),
       (5, 3, 500),
       (2, 4, 500),
       (3, 4, 100),
       (3, 5, 200),
       (4, 5, 300),
       (5, 5, 100);

