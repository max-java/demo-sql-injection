create sequence hibernate_sequence;
create table person(
    id int,
    name varchar(8),
    secret varchar(5)
);
insert into person values (1, 'mikas', 'MB003'), (2, 'max-java', 'VC439'), (3, 'tutrit', 'RS343');