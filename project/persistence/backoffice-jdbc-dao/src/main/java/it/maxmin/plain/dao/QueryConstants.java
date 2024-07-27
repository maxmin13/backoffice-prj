package it.maxmin.plain.dao;

public enum QueryConstants {
    ;

	public static final String SELECT_ALL_USERS = "select * from User";
	public static final String FIND_USER_BY_ACCOUNT_NAME = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where AccountName = ?";
    public static final String FIND_USER_BY_FIRST_NAME = "select UserId, AccountName, FirstName, LastName, BirthDate, CreatedDate from User where FirstName = ?";
    public static final String INSERT_USER = "insert into User (AccountName, FirstName, LastName, BirthDate) values (?,?,?,?)";
    
    public static final String UPDATE_USER = "update User set AccountName=?, FirstName=?, LastName=?, BirthDate=? where UserId=?";
    
    public static final String DELETE_USER = "delete from User where UserId=?";
       
//    public static final String ALL_JOIN_SELECT = "select s.id, s.first_name, s.last_name, s.birth_date, "+
//            "a.id AS album_id, a.title, a.release_date " +
//            "from SINGER s " +
//            "left join ALBUM a on s.id = a.singer_id";


}
