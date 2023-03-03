-- create database acme_bank;

-- use acme_bank;

-- create table if not exists accounts (
-- 	`account_id` varchar(10) not null,
--     `name` char(15) not null,
--     `balance` decimal(10,2) not null
-- 		PRIMARY KEY (`account_id`);
-- );

-- V9L3Jd1BBI, fred, 100.00
-- fhRq46Y6vB, barney, 300.00
-- uFSFRqUpJy, wilma, 1000.00
-- ckTV56axff, betty, 1000.00
-- Qgcnwbshbh, pebbles, 50.00
-- if9l185l18, bambam, 50.00

insert into accounts (`account_id`, `name`, `balance`) values ('V9L3Jd1BBI', 'fred', 100.00);
insert into accounts (`account_id`, `name`, `balance`) values ('fhRq46Y6vB', 'barney', 300.00);
insert into accounts (`account_id`, `name`, `balance`) values ('uFSFRqUpJy', 'wilma', 1000.00);
insert into accounts (`account_id`, `name`, `balance`) values ('ckTV56axff', 'betty', 1000.00);
insert into accounts (`account_id`, `name`, `balance`) values ('Qgcnwbshbh', 'pebbles', 50.00);
insert into accounts (`account_id`, `name`, `balance`) values ('if9l185l18', 'bambam', 50.00);
 

