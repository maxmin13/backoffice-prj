DELETE FROM State WHERE StateId IN (SELECT StateId FROM User WHERE Name = 'Italy');
DELETE FROM State WHERE StateId IN (SELECT StateId FROM User WHERE Name = 'Ireland');