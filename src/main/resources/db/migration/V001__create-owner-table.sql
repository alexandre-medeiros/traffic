create table owner (
  id bigint not null auto_increment,
  name varchar(60) not null,
  email varchar(255) not null,
  phone varchar(20) not null,

  primary key (id)
) AUTO_INCREMENT = 1;

alter table owner
add constraint uk_owner unique (email);