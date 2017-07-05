package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DBManager<T> implements DBAccess<T> {

	private String dbName;
	private final String dbTable;
	
	private String dbUri;
	private Connection connect;
	
	//TODO ojo no sera miembro
	
	private ResultSet resultSet;
	
	
	public DBManager(String dbHost, String dbName, String dbTable){
		this.dbTable = dbTable;
		this.dbName = dbName;
		this.dbUri= "jdbc:mysql://host/dbName?user=root&password=12345";
		dbUri= dbUri.replace("host",dbHost).
				replace("dbName",dbName);
	}
	
	
	@Override
	public void connect(String user, String password) 
			throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		try {
			String uri= dbUri.replace("root",user)
					.replaceAll("12345", password);
			
			// Cargar el driver MYSQL
			Class.forName("com.mysql.jdbc.Driver");
			// jdbc:mysql://ip database // database ?
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/"+ dbName +"?"
							+ "user=root&password=");
			// Statements allow to issue SQL queries to the Database
			// statement= connect.createStatement();
		}catch (ClassNotFoundException e){
			throw e;
		}catch (Exception e){
			close();
			throw e;
		}
	}

	


	@Override
	public  abstract T insert(T object) throws SQLException; {
		// TODO Auto-generated method stub
	}

	@Override
	public abstract void update(T object) throws SQLException; 

	@Override
	public abstract  T select(int id) throws SQLException;
	
	@Override
	public  abstract ArrayList<T> select(String column, String value) throws SQLException;

	@Override
	public void delete(int id) throws SQLException {
		// TODO 
		PreparedStatement preparedStatement=null;
		try{
			preparedStatement = connect
					.prepareStatement("delete from "+ dbTable +" where id= ?");
			preparedStatement.executeUpdate();
			
		}catch (SQLException e){
			close();
			throw e;
		}finally{
			close();
		}
	}
	
	
	@Override
	public void deleteAll() throws SQLException{
		PreparedStatement preparedStatement=null;
		try{
			preparedStatement = connect
					.prepareStatement("TRUNCATE "+ dbTable);
			preparedStatement.executeUpdate();
			
		}catch (SQLException e){
			close();
			throw e;
		}finally{
			close();
		}
	}
	
	@Override
	public void close() {
		try{
 			if (resultSet !=null){
 				resultSet.close();
 				resultSet=null;
			}
						
			if (connect !=null){
				connect.close();
				connect=null;
			}
		}catch (Exception e){
		
		}
	}
	
	
	
	//  getters y setters (metodos accesorios)

	public String getDbName() {
		return dbName;
	}
	
	public String getDbTable() {
		return dbTable;
	}
	
	protected Connection getConnected(){
		return connect;
	}
		
	
	 
	 
}
