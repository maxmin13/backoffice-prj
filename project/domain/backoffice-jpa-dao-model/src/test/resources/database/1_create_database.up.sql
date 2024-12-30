CREATE TABLE Department (Id INT(11), Name VARCHAR(60) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), UNIQUE (Name));
CREATE SEQUENCE UserSeq START WITH 100 INCREMENT BY 1;
CREATE TABLE User (Id INT(11), AccountName VARCHAR(60) NOT NULL, FirstName VARCHAR(60) NOT NULL, LastName VARCHAR(60) NOT NULL, DepartmentId INT(11) NOT NULL, BirthDate DATE NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), UNIQUE (AccountName), CONSTRAINT fk_user_department FOREIGN KEY (DepartmentId) REFERENCES Department (Id));
CREATE TABLE Role (Id INT(11), Name VARCHAR(20) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), UNIQUE (Name));
CREATE TABLE UserRole (UserId INT(11), RoleId INT(11), CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (UserId, RoleId), CONSTRAINT fk_userrole_user FOREIGN KEY (UserId) REFERENCES User (Id), CONSTRAINT fk_userrole_role FOREIGN KEY (RoleId) REFERENCES Role (Id));
CREATE TABLE UserStatus (Id INT, UserId INT (11) NOT NULL, Active TINYINT (1) NOT NULL CHECK (Active >= 0 AND Active <= 1), CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), CONSTRAINT fk_user_status_user_id FOREIGN KEY (UserId) REFERENCES User(Id));
CREATE SEQUENCE UserPasswordSeq START WITH 100 INCREMENT BY 1;
CREATE TABLE UserPassword (Id INT, UserId INT (11) NOT NULL, Value VARCHAR(100) NOT NULL, EffDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, EndDate TIMESTAMP, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), UNIQUE (UserId, EffDate), CONSTRAINT fk_user_password_user_id FOREIGN KEY (UserId) REFERENCES User (Id));
CREATE SEQUENCE LoginAttemptSeq START WITH 100 INCREMENT BY 1;
CREATE TABLE LoginAttempt (Id INT, UserId INT(11) NOT NULL, Success TINYINT(1) NOT NULL CHECK (Success >= 0 AND Success <= 1), LoginAt TIMESTAMP NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY  (Id), UNIQUE (UserId, LoginAt), CONSTRAINT fk_loginattempt_user FOREIGN KEY (UserId) REFERENCES User (Id));
CREATE TABLE State (Id INT(11) NOT NULL, Name VARCHAR(60) NOT NULL, Code CHAR(2) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), UNIQUE (Code), UNIQUE (Name));
CREATE SEQUENCE AddressSeq START WITH 100 INCREMENT BY 1;
CREATE TABLE Address (Id INT(11), Description VARCHAR(120) NOT NULL, City VARCHAR(100) NOT NULL, StateId INT(11) NOT NULL, Region VARCHAR(100) NOT NULL, PostalCode VARCHAR(16) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), UNIQUE (PostalCode), CONSTRAINT fk_address_state FOREIGN KEY (StateId) REFERENCES State (Id));
CREATE TABLE UserAddress (UserId INT(11), AddressId INT(11), CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (UserId, AddressId), CONSTRAINT fk_user_address_user FOREIGN KEY (UserId) REFERENCES User(Id), CONSTRAINT fk_user_address_address FOREIGN KEY (AddressId) REFERENCES Address(Id));
CREATE TABLE TransactionType (Id INT, Type VARCHAR(30) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), UNIQUE (Type));
CREATE TABLE AccountStatus (Id INT, Status VARCHAR(20) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), UNIQUE (Status));
CREATE TABLE AccountType (Id INT, Type VARCHAR(30) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), PRIMARY KEY (Id), UNIQUE (Type));
CREATE TABLE Account (Id INT(11), AccountNumber VARCHAR(30) NOT NULL, UserId INT(11) NOT NULL, AccountTypeId INT(11) NOT NULL, AccountStatusId INT(11) NOT NULL, CurrentBalance DECIMAL(10,2) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, ClosedAt TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), UNIQUE (AccountNumber), CONSTRAINT fk_account_user_id FOREIGN KEY (UserId) REFERENCES User (Id), CONSTRAINT fk_account_account_type_id FOREIGN KEY (AccountTypeId) REFERENCES AccountType(Id), CONSTRAINT fk_account_account_status_id FOREIGN KEY (AccountStatusId) REFERENCES AccountStatus(Id));
CREATE TABLE Transaction (Id INT, AccountId INT (11) NOT NULL, TransactionTypeId INT (11) NOT NULL, Amount DECIMAL(10,2) NOT NULL, CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CreatedBy VARCHAR(60), Version INT NOT NULL DEFAULT 0, PRIMARY KEY (Id), CONSTRAINT fk_transaction_account_id FOREIGN KEY (AccountId) REFERENCES Account(Id), CONSTRAINT fk_transaction_transaction_type_id FOREIGN KEY (TransactionTypeId) REFERENCES TransactionType(Id));