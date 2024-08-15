INSERT INTO User (AccountName, FirstName, LastName, BirthDate) VALUES ('maxmin13', 'Max', 'Minardi', '1977-10-16');
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT UserId FROM User WHERE AccountName = 'maxmin13'), (SELECT UserRoleId FROM UserRole WHERE RoleName = 'administrator'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT UserId FROM User WHERE AccountName = 'maxmin13'), 1);
INSERT INTO UserPassword (UserId, Password) VALUES ((SELECT UserId FROM User WHERE AccountName = 'maxmin13'), 'secret');
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT UserId FROM User WHERE AccountName = 'maxmin13'),(SELECT AddressId FROM Address WHERE Address = 'Via borgo di sotto'));
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT UserId FROM User WHERE AccountName = 'maxmin13'),(SELECT AddressId FROM Address WHERE Address = 'Connolly street'));
   
INSERT INTO User (AccountName, FirstName, LastName, BirthDate) VALUES ('artur', 'art', 'artur', '1923-10-12');
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT UserId FROM User WHERE AccountName = 'artur'), (SELECT UserRoleId FROM UserRole WHERE RoleName = 'administrator'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT UserId FROM User WHERE AccountName = 'artur'), 1);
INSERT INTO UserPassword (UserId, Password) VALUES ((SELECT UserId FROM User WHERE AccountName = 'artur'), 'secret');
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT UserId FROM User WHERE AccountName = 'artur'), (SELECT AddressId FROM Address WHERE Address = 'Connolly street'));

INSERT INTO User (AccountName, FirstName, LastName, BirthDate) VALUES ('reginald123', 'reginald', 'reinold', '1944-12-23');
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT UserId FROM User WHERE AccountName = 'reginald123'), (SELECT UserRoleId FROM UserRole WHERE RoleName = 'user'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT UserId FROM User WHERE AccountName = 'reginald123'), 1);
INSERT INTO UserPassword (UserId, Password) VALUES ((SELECT UserId FROM User WHERE AccountName = 'reginald123'), 'secret');

  
