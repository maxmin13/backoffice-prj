INSERT INTO Address (Description, City, StateId, Region, PostalCode) VALUES ("Via borgo di sotto", "Rome", (SELECT Id FROM State WHERE Name = 'Italy'), "Lazio", "30010");
INSERT INTO Address (Description, City, StateId, Region, PostalCode) VALUES ("Via Roma", "Venice", (SELECT Id FROM State WHERE Name = 'Italy'), "Veneto", "31210");
INSERT INTO Address (Description, City, StateId, Region, PostalCode) VALUES ("Connolly street", "Dublin", (SELECT Id FROM State WHERE Name = 'Ireland'), "County Dublin", "A65TF12");