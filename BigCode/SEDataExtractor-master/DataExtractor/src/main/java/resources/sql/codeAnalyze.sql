drop table if exists code_class;
create table code_class(
	code_class_id integer not null AUTO_INCREMENT,
	code_file_id integer,
	code_package varchar(100),
	class_name varchar(100),
	content text,
	primary key(code_class_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists code_method;
create table code_method(
	code_method_id integer not null AUTO_INCREMENT,
	code_class_id integer, 
	method_signature text,
	method_name varchar(500),
	access varchar(50),
	modifier varchar(20),
	primary key(code_method_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists code_parameter;
create table code_parameter(
	code_parameter_id integer not null AUTO_INCREMENT,
	code_method_id integer,
    parameter_name varchar(100),
    parameter_scope varchar(20),
	parameter_type varchar(100),
	type_value varchar(500),
	inner_class integer,
	api integer,
	primary key(code_parameter_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists code_import;
create table code_import(
	code_import_id integer not null AUTO_INCREMENT,
	code_class_id integer,
	import_package varchar(200),
	import_class varchar(100),
	import_type varchar(100),
	inner_class integer,
	api integer,
	primary key(code_import_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists code_field;
create table code_field(
	code_field_id integer not null AUTO_INCREMENT,
    class_id integer,
	field_name varchar(100),
	field_type varchar(100),
	type_value varchar(500),
	inner_class integer,
	api integer,
	primary key(code_field_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;









