INSERT INTO cuisine (name) VALUES ('Thai');
INSERT INTO cuisine (name) VALUES ('Brazilian');
INSERT INTO cuisine (name) VALUES ('Indian');
INSERT INTO cuisine (name) VALUES ('German');
INSERT INTO cuisine (name) VALUES ('Fast Food');

-- Inserirr múltiplos restaurantes
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Mc Donalds', 15, 5);
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Burger King', 5, 5);
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Fogo de chão', 17.5, 2);
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Thai delivery food', 6, 1);
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Curry delicious delivery', 9, 3);
INSERT INTO restaurant (name, delivery_fee, cuisine_id ) VALUES ('Oktoberfest Restaurant', 27, 4);

-- Inserir múltiplos estados
INSERT INTO state (name) VALUES ('São Paulo');
INSERT INTO state (name) VALUES ('Rio de Janeiro');
INSERT INTO state (name) VALUES ('Minas Gerais');
INSERT INTO state (name) VALUES ('Bahia');
INSERT INTO state (name) VALUES ('Paraná');

-- Supondo que os IDs dos estados são 1, 2, 3, 4, e 5, inserir múltiplas cidades
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
