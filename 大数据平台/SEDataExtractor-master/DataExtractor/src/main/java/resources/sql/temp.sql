drop table if exists unexpectedRelease;
create table unexpectedRelease(
	id integer not null AUTO_INCREMENT,
	release_id integer,
	state varchar(100),
	primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;