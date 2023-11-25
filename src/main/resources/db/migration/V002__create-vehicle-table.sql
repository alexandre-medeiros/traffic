create table vehicle(
	id bigint not null auto_increment,
    owner_id bigint not null,
    make varchar(20) not null,
    model varchar(20) not null,
    plate varchar(7) not null,
    status varchar(20) not null,
    created_date datetime not null,
    arrested_date datetime,

    primary key (id)
);

alter table vehicle add constraint fk_vehicle_owner_id
foreign key (owner_id) references owner (id);

alter table vehicle add constraint uk_vehicle unique (plate);