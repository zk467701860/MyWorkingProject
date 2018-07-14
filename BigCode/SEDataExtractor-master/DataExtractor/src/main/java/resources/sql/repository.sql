drop table if exists project;
create table project(
	project_id integer not null AUTO_INCREMENT,
	repository_id integer,
	project_name varchar(100),
	project_type varchar(100),
	orgnization varchar(200),
	main_page varchar(200),
	description_address varchar(200),
	description text,
	primary key(project_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists new_repository;
create table new_repository(
	repository_id integer not null AUTO_INCREMENT,
	project_id integer,
	repository_name varchar(100),
	repository_type varchar(20),
	remote_address varchar(200),
	issue_address varchar(200),
	license text,
	last_modified_date date,
	primary key(repository_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists project_release;
create table project_release(
	release_id integer not null AUTO_INCREMENT,
	project_id integer,
	version varchar(100),
	remote_path varchar(200),
	local_path varchar(200),
	zip_local_path varchar(200),
	cteated_date date,
	primary key(release_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists code_file;
create table code_file(
	file_id integer not null AUTO_INCREMENT,
	release_id integer,
	file_name varchar(100),
	relative_address varchar(100),
	primary key(file_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;










