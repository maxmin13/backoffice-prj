INSERT INTO User (AccountName, FirstName, LastName, BirthDate, DepartmentId) VALUES ('maxmin13', 'Max', 'Minardi', '1977-10-16', (SELECT Id FROM Department WHERE name = "Production"));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'maxmin13'), (SELECT Id FROM UserRole WHERE RoleName = 'Administrator'));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'maxmin13'), (SELECT Id FROM UserRole WHERE RoleName = 'User'));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'maxmin13'), (SELECT Id FROM UserRole WHERE RoleName = 'Worker'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT Id FROM User WHERE AccountName = 'maxmin13'), 1);
INSERT INTO UserPassword (UserId, Value, EffDate) VALUES ((SELECT Id FROM User WHERE AccountName = 'maxmin13'), 'secret', CURRENT_TIMESTAMP());
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT Id FROM User WHERE AccountName = 'maxmin13'),(SELECT Id FROM Address WHERE Description = 'Via borgo di sotto'));
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT Id FROM User WHERE AccountName = 'maxmin13'),(SELECT Id FROM Address WHERE Description = 'Connolly street'));
INSERT INTO User (AccountName, FirstName, LastName, BirthDate, DepartmentId) VALUES ('artur', 'Arturo', 'Art', '1923-10-12', (SELECT Id FROM Department WHERE name = "Legal"));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'artur'), (SELECT Id FROM UserRole WHERE RoleName = 'Administrator'));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'artur'), (SELECT Id FROM UserRole WHERE RoleName = 'User'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT Id FROM User WHERE AccountName = 'artur'), 1);
INSERT INTO UserPassword (UserId, Value, EffDate) VALUES ((SELECT Id FROM User WHERE AccountName = 'artur'), 'secret', CURRENT_TIMESTAMP());
INSERT INTO UserAddress (UserId, AddressId) VALUE ((SELECT Id FROM User WHERE AccountName = 'artur'), (SELECT Id FROM Address WHERE Description = 'Connolly street'));
INSERT INTO User (AccountName, FirstName, LastName, BirthDate, DepartmentId) VALUES ('reginald123', 'reginald', 'reinold', '1944-12-23', (SELECT Id FROM Department WHERE name = "Accounts"));
INSERT INTO UserUserRole (UserId, UserRoleId) VALUES ((SELECT Id FROM User WHERE AccountName = 'reginald123'), (SELECT Id FROM UserRole WHERE RoleName = 'User'));
INSERT INTO UserStatus (UserId, Active) VALUES ((SELECT Id FROM User WHERE AccountName = 'reginald123'), 1);
INSERT INTO UserPassword (UserId, Value, EffDate) VALUES ((SELECT Id FROM User WHERE AccountName = 'reginald123'), 'secret', CURRENT_TIMESTAMP());