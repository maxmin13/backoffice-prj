insert into User (AccountName, FirstName, LastName, BirthDate) values ('maxmin13', 'Max', 'Minardi', '1977-10-16');
insert into UserUserRole (UserId, UserRoleId) values ((select UserId from User where AccountName = 'maxmin13'), (select UserRoleId from UserRole where RoleName = 'administrator'));
insert into UserStatus (UserId, Active) values ((select UserId from User where AccountName = 'maxmin13'), 1);
insert into UserPassword (UserId, Password) values ((select UserId from User where AccountName = 'maxmin13'), 'secret');
insert into User (AccountName, FirstName, LastName, BirthDate) values ('artur', 'art', 'artur', '1923-10-12');
insert into UserUserRole (UserId, UserRoleId) values ((select UserId from User where AccountName = 'artur'), (select UserRoleId from UserRole where RoleName = 'administrator'));
insert into UserStatus (UserId, Active) values ((select UserId from User where AccountName = 'artur'), 1);
insert into UserPassword (UserId, Password) values ((select UserId from User where AccountName = 'artur'), 'secret');
