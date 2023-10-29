create table owner (
  id bigint not null auto_increment,
  name varchar(60) not null,
  email varchar(255) not null,
  telephone varchar(20) not null,

  primary key (id)
);

alter table owner
add constraint uk_owner unique (email);