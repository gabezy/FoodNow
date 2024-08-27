BEGIN;

-- Disable foreign key constrains
SET session_replication_role = 'replica';

DELETE FROM city ;
DELETE FROM restaurant ;
DELETE FROM cuisine ;
DELETE FROM group_ ;
DELETE FROM payment_method ;
DELETE FROM product ;
DELETE FROM user_group ;
DELETE FROM user_ ;
DELETE FROM group_role ;
DELETE FROM restaurant_payment_method ;
DELETE FROM state ;
DELETE FROM "role" ;

-- Re-enable foreign key constrains
SET session_replication_role = 'origin';

DELETE FROM city ;
DELETE FROM restaurant ;
DELETE FROM cuisine ;
DELETE FROM group_ ;
DELETE FROM payment_method ;
DELETE FROM product ;
DELETE FROM user_group ;
DELETE FROM user_ ;
DELETE FROM group_role ;
DELETE FROM restaurant_payment_method ;
DELETE FROM state ;
DELETE FROM "role" ;

-- Reset sequence for city table
ALTER SEQUENCE city_id_seq RESTART WITH 1;

-- Reset sequence for restaurant table
ALTER SEQUENCE restaurant_id_seq RESTART WITH 1;

-- Reset sequence for cuisine table
ALTER SEQUENCE cuisine_id_seq RESTART WITH 1;

-- Reset sequence for group_ table
ALTER SEQUENCE group__id_seq RESTART WITH 1;

-- Reset sequence for payment_method table
ALTER SEQUENCE payment_method_id_seq RESTART WITH 1;

-- Reset sequence for product table
ALTER SEQUENCE product_id_seq RESTART WITH 1;

-- Reset sequence for user_ table
ALTER SEQUENCE user__id_seq RESTART WITH 1;

-- Reset sequence for state table
ALTER SEQUENCE state_id_seq RESTART WITH 1;

-- Reset sequence for "role" table
ALTER SEQUENCE role_id_seq RESTART WITH 1;

COMMIT;

-- Insert Restaurants
INSERT INTO cuisine (name) VALUES ('Thai');
INSERT INTO cuisine (name) VALUES ('Brazilian');
INSERT INTO cuisine (name) VALUES ('Indian');
INSERT INTO cuisine (name) VALUES ('German');
INSERT INTO cuisine (name) VALUES ('Fast Food');

-- Insert Stated
INSERT INTO state (name) VALUES ('São Paulo');
INSERT INTO state (name) VALUES ('Rio de Janeiro');
INSERT INTO state (name) VALUES ('Minas Gerais');
INSERT INTO state (name) VALUES ('Bahia');
INSERT INTO state (name) VALUES ('Paraná');

-- Insert cities
INSERT INTO city (name, state_id) VALUES ('São Paulo', 1);
INSERT INTO city (name, state_id) VALUES ('Campinas', 1);
INSERT INTO city (name, state_id) VALUES ('Rio de Janeiro', 2);
INSERT INTO city (name, state_id) VALUES ('Niterói', 2);
INSERT INTO city (name, state_id) VALUES ('Belo Horizonte', 3);
INSERT INTO city (name, state_id) VALUES ('Uberlândia', 3);
INSERT INTO city (name, state_id) VALUES ('Salvador', 4);
INSERT INTO city (name, state_id) VALUES ('Feira de Santana', 4);
INSERT INTO city (name, state_id) VALUES ('Curitiba', 5);
INSERT INTO city (name, state_id) VALUES ('Londrina', 5);

-- Insert restaurants
INSERT INTO restaurant (name, delivery_fee, cuisine_id, address_city_id, address_zipcode, address_street, address_number, created_at) VALUES ('Mc Donalds', 15, 5, 1, '38400-999', 'Rua João Pinheiro', '1000', CURRENT_TIMESTAMP);
INSERT INTO restaurant (name, delivery_fee, cuisine_id, created_at ) VALUES ('Burger King', 5, 5, CURRENT_TIMESTAMP);
INSERT INTO restaurant (name, delivery_fee, cuisine_id, created_at ) VALUES ('Fogo de chão', 17.5, 2, CURRENT_TIMESTAMP);
INSERT INTO restaurant (name, delivery_fee, cuisine_id, created_at) VALUES ('Thai delivery food', 6, 1, CURRENT_TIMESTAMP);
INSERT INTO restaurant (name, delivery_fee, cuisine_id, created_at ) VALUES ('Curry delicious delivery', 9, 3, CURRENT_TIMESTAMP);
INSERT INTO restaurant (name, delivery_fee, cuisine_id, created_at ) VALUES ('Oktoberfest Restaurant', 27, 4, CURRENT_TIMESTAMP);

-- Inserindo forma de pagamento
INSERT INTO payment_method (description) VALUES ('Cartão de crédito');
INSERT INTO payment_method (description) VALUES ('Cartão de débito');
INSERT INTO payment_method (description) VALUES ('Dinheiro');

-- Insert payment methods
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id) VALUES (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);

-- Insert Product
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Big mac', 'Big mac com molho especial (somente o hamburger)', 20.99, true, 1);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Big mac combo', 'Big mac com bebida e batata média', 45.99, true, 1);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Chicken curry with special sauce', 'Chicken Curry with secret sauce made by mother Thai', 33.50, true, 4);

-- Insert roles
INSERT INTO role (name, description) VALUES ('CONSULTAR_COZINHAS', 'permite consultar cozinhas'); -- id: 1
INSERT INTO role (name, description) VALUES ('EDITAR_COZINHAS', 'permite editar cozinhas'); -- id: 2

-- Insert Group
INSERT INTO group_ (name) VALUES ('Admin'); -- id: 1
INSERT INTO group_ (name) VALUES ('Operators'); -- id: 2

-- Insert Group role
INSERT INTO group_role (group_id, role_id) VALUES (1, 1), (1, 2), (2, 1);

-- INSERT USER
INSERT INTO user_ (name, email, password) VALUES ('Jonh Doe', 'jonhdoe@email.com', 'password'); -- id: 2
INSERT INTO user_ (name, email, password) VALUES ('Jonh Josh', 'jonhjosh@email.com', 'password'); -- id: 2
INSERT INTO user_ (name, email, password) VALUES ('Jonh kennedy', 'jonhkennedy@email.com', 'password'); -- id: 3

-- INSERT USER_GROUP
INSERT INTO user_group (user_id, group_id) VALUES (1,1);
INSERT INTO user_group (user_id, group_id) VALUES (2,2);
INSERT INTO user_group (user_id, group_id) VALUES (3,2);
