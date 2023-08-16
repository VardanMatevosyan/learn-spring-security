-- Test User
-- test@email.com/pass
delete from  lss33.user;

insert into lss33.user (id, email, password, enabled, created) values (1, 'test@email.com', '$2a$04$kqRvgmJBlWZQQ2c9NT9IH.ZhxFY07Y2xE73vmLHxBq2hNTvGvUc5m', true, '2008-08-08 00:00:00');

create table if not exists lss33.persistent_logins (
 username varchar(64) not null,
 series varchar(64) primary key,
 token varchar(64) not null,
 last_used timestamp not null
);
