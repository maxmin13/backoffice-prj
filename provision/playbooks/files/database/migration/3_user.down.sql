DELETE FROM Password WHERE UserId IN (select UserId from User where AccountName = 'maxmin13');
DELETE FROM UserUserRole WHERE UserId IN (select UserId from User where AccountName = 'maxmin13');
DELETE FROM User WHERE AccountName = 'maxmin13'