create table state (
    id bigserial primary key,
    name varchar(80) not null
);

-- insert into state (name) select distinct state_name from city ;

alter table city add column state_id bigint not null default 0;

-- update city set state_id = (select id from state where city.state_name = state.name);

alter table city add constraint fk_state_city foreign key (state_id) references state (id);

alter table city drop column state_name;

alter table city rename column city_name to name;

-- COMMENTS
comment on table cuisine is 'Tabela de cozinha';
comment on table city is 'Tabela de cidades';
comment on table state is 'Tabela de estados';
comment on column city.state_id is 'Coluna referente o ID do estado (FK)';
