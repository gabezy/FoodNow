create table order_ (
    id bigserial,
    subtotal numeric(38, 2) not null,
    delivery_fee numeric(38, 2) not null,
    total numeric(38, 2) not null,
    created_at timestamp(6) not null,
    confirmed_at timestamp(6),
    canceled_at timestamp(6),
    delivered_at timestamp(6),
    address_zipcode varchar(15) not null,
    address_street varchar(50) not null,
    address_number varchar (10) not null,
    address_city_id bigint not null,
    order_status varchar(20) not null,
    payment_method_id bigint not null,
    user_id bigint not null,
    restaurant_id bigint not null,
    primary key (id)
);

create table item_order (
    id bigserial,
    quantity int not null,
    unit_price numeric(38, 2) not null,
    total_price numeric(38, 2) not null,
    observation varchar(150),
    order_id bigint not null,
    product_id bigint not null,
    primary key (id)
);

-- ORDERS CONSTRAINTS
alter table if exists order_ add constraint fk_address_city_id_order foreign key (address_city_id) references city (id);
alter table if exists order_ add constraint fk_payment_method_id_order foreign key (payment_method_id) references payment_method (id);
alter table if exists order_ add constraint fk_user_id_order foreign key (user_id) references user_ (id);
alter table if exists order_ add constraint fk_restaurant_id_order foreign key (restaurant_id) references restaurant (id);


-- ITEM_ORDER CONSTRAINTS
alter table if exists item_order add constraint fk_order_id_item_order foreign key (order_id) references order_ (id);
alter table if exists item_order add constraint fk_product_id_item_order foreign key (product_id) references product (id);

comment on table order_ is 'Tabela de pedidos';
comment on table item_order is 'Tabela de items do pedido'

