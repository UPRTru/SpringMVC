insert into items (name, price)
values
    ('item', 100);

insert into items (name, price)
values
    ('item2', 200);

insert into users (name, email)
values
    ('user', 'user@email.com');

insert into orders (user_name, amount, status)
values
    ('user', 100.0, 'UNPAID');

insert into orders (user_name, amount, status)
values
    ('user', 200.0, 'UNPAID');

insert into order_items (order_id, item_id)
values
    (1, 1);

insert into order_items (order_id, item_id)
values
    (2, 2);