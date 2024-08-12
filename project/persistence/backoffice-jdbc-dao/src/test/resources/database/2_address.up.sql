INSERT INTO Address (Address, City, StateId, Region, PostalCode) VALUES ("Via borgo di sotto", "Rome", (SELECT StateId FROM State WHERE Name = 'Italy'), "Lazio", "30010");
INSERT INTO Address (Address, City, StateId, Region, PostalCode) VALUES ("Connolly street", "Dublin", (SELECT StateId FROM State WHERE Name = 'Ireland'), "County Dublin", "A65TF12");
