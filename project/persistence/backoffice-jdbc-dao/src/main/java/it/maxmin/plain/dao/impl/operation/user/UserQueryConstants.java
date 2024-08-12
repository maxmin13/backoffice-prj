package it.maxmin.plain.dao.impl.operation.user;

public enum UserQueryConstants {
    ;

	public static final String SELECT_ALL_USERS = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User";
	public static final String SELECT_USER_BY_ACCOUNT_NAME = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where AccountName = :accountName";
    public static final String SELECT_USER_BY_FIRST_NAME = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where FirstName = :firstName";
    public static final String UPDATE_USER = "update User set AccountName=:accountName, FirstName=:firstName, LastName=:lastName, BirthDate=:birthData where UserId=:userId";

//    public static final String ALL_JOIN_SELECT = "select s.id, s.first_name, s.last_name, s.birth_date, "+
//            "a.id AS album_id, a.title, a.release_date " +
//            "from SINGER s " +
//            "left join ALBUM a on s.id = a.singer_id";


}
