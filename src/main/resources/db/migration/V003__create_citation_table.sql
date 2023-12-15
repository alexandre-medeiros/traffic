create table citation (
	id bigint not null auto_increment,
    vehicle_id bigint not null,
    description text not null,
    amount decimal(10,2) not null,
    date_violation datetime not null,
    primary key (id)
) AUTO_INCREMENT = 1;

alter table citation add constraint fk_citation_vehicle
foreign key (vehicle_id) references vehicle (id);