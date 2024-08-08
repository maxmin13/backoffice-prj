insert into User (AccountName, FirstName, LastName, BirthDate) values ('maxmin13', 'Max', 'Minardi', '1977-10-16');
insert into UserUserRole (UserId, UserRoleId) values ((select UserId from User where AccountName = 'maxmin13'), (select UserRoleId from UserRole where RoleName = 'administrator'));
insert into UserStatus (UserId, Active) values ((select UserId from User where AccountName = 'maxmin13'), 1);
insert into UserPassword (UserId, Password) values ((select UserId from User where AccountName = 'maxmin13'), 'secret');

