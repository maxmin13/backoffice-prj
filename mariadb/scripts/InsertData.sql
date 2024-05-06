
insert into UserRole (RoleName, Active) values ('administrator', 1);
insert into UserRole (RoleName, Active) values ('user', 1);

insert into User (AccountName, FirstName, LastName, BirthDate) values ('maxmin13', 'Max', 'Minardi', '1977-10-16');
insert into UserUserRole (UserId, UserRoleId) values ((select UserId from User where AccountName = 'maxmin13'), (select UserRoleId from UserRole where RoleName = 'administrator'));
insert into Password (UserId, Active, Value) values ((select UserId from User where AccountName = 'maxmin13'), 1, 'default'); 
   
/* view the inserted data */
SELECT * FROM User;
SELECT * FROM UserRole;
SELECT * FROM Password;

