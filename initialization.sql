create user test with encrypted password 'test';
create database alten_test;
grant all privileges on database alten_test to test;
alter database alten_test OWNER to test;
