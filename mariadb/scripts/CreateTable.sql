CREATE TABLE User (
       UserId INT NOT NULL AUTO_INCREMENT
     , AccountName VARCHAR(60) NOT NULL
     , FirstName VARCHAR(60) NOT NULL
     , LastName VARCHAR(40) NOT NULL
     , BirthDate DATE NOT NULL
     , CreatedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , PRIMARY KEY (UserId)
     , UNIQUE (AccountName)
     , UNIQUE (FirstName, LastName)
);

CREATE TABLE UserRole (
       UserRoleId INT NOT NULL AUTO_INCREMENT
     , RoleName VARCHAR(20) NOT NULL
     , Active BOOLEAN NOT NULL
     , CreatedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , PRIMARY KEY (UserRoleId)
     , UNIQUE (RoleName)
);

CREATE TABLE UserUserRole (
       UserId INT NOT NULL
     , UserRoleId INT NOT NULL
     , CreatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     , PRIMARY KEY (UserId, UserRoleId)
     , CONSTRAINT Fk_UserUserRole_User FOREIGN KEY (UserId) REFERENCES User (UserId)
     , CONSTRAINT Fk_UserUserRole_User_Role FOREIGN KEY (UserRoleId) REFERENCES UserRole (UserRoleId)                  
);

CREATE TABLE Password (
       PasswordId INT NOT NULL AUTO_INCREMENT
     , UserId INT NOT NULL
     , Value VARCHAR(80)
     , Active BOOLEAN NOT NULL
     , CreatedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , PRIMARY KEY (PasswordId)
     , CONSTRAINT Fk_Password_User FOREIGN KEY (UserId) REFERENCES User (UserId)
);


CREATE TABLE LoginAttempt (
       LoginAttemptId INT NOT NULL AUTO_INCREMENT
     , AccountName VARCHAR(60) NOT NULL
     , Success BOOLEAN NOT NULL
     , CreatedDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , PRIMARY KEY (LoginAttemptId)
     , CONSTRAINT Fk_LoginAttempt_User FOREIGN KEY (AccountName) REFERENCES User (AccountName)
);

