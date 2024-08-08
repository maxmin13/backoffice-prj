DELETE FROM UserUserRole WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'maxmin13');
DELETE FROM UserStatus WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'maxmin13');
DELETE FROM UserPassword WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'maxmin13');
DELETE FROM User WHERE AccountName = 'maxmin13';
DELETE FROM UserUserRole WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'artur');
DELETE FROM UserStatus WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'artur');
DELETE FROM UserPassword WHERE UserId IN (SELECT UserId FROM User WHERE AccountName = 'artur');
DELETE FROM User WHERE AccountName = 'artur';
