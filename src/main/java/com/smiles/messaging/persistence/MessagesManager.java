/**
 * 
 */
package com.smiles.messaging.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.smiles.messaging.models.Configs;
import com.smiles.messaging.models.User;

/**
 * @author yaojliu
 *
 */
public class MessagesManager {
	
	private static DruidDataSource _dataSource;
	
	private static final Logger LOG = LoggerFactory.getLogger(MessagesManager.class);
	private static final MessagesManager instance = new MessagesManager();
	
	private static final String SCRIPTS_CREATE_TABLE_USER = "CREATE TABLE user ("
			+ "userId VARCHAR(50), "
			+ "alias VARCHAR(50), "
			+ "phone VARCHAR(50), "
			+ "email VARCHAR(50), "
			+ "createTime DATETIME "
			+ ");"
			+ "CREATE INDEX idx_user_alias on user(alias);"
			+ "CREATE INDEX idx_user_userId on user(userId);"
			+ "CREATE INDEX idx_user_createTime on user(createTime);";

	private static final String SCRIPTS_CREATE_TABLE_SERVER_INFO = "CREATE TABLE server_info ("
			+ "server_id VARCHAR(50)" + "); ";

	/**
	 * 
	 */
	private MessagesManager() {
		// TODO Auto-generated constructor stub
		try {
			init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error("MessagesManager init fails : " + e.getCause());
		}
	}
	
	public static MessagesManager getInstance(){
		return instance;
	}
	
	public User getUserByUserId(String userId) {
		String sql = "SELECT * from user where userId = '" + userId + "'";
		Statement st = null;
		User user = null;
		try {
			st = getStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				user = fromResultSetUser(rs);
			}
		} catch (SQLException e) {
			LOG.error("query user error", e);
		} finally {
			try {
				Connection conn = st.getConnection();
				st.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return user;
	}
	
	public User getUserByAlias(String alias) {
        String sql = "SELECT * from user where alias = '" + alias + "'";
        Statement st = null;
        User user = null;
        try {
            st = getStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                user = fromResultSetUser(rs);
            }
        } catch (SQLException e) {
            LOG.error("query user error", e);
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }
        }
        
        return user;
    }
    
    public int insertUser(User user) {
    	String sql = "INSERT INTO user (userId, alias, createTime) values (?, ?, ?)";
        PreparedStatement st = null;
    	try {
    		st = getPreparedStatement(sql);
    		st.setString(1, user.getUserId());
    		st.setString(2, user.getAlias());
    		st.setTimestamp(3, new Timestamp(user.getCreateTime().getTime()));
	        return st.executeUpdate();
    	} catch (SQLException e) {
    		LOG.error("insert user error", e);
    		return -1;
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public boolean deleteUser(String userId) {
    	String sql = "DELETE from user WHERE userId = '" + userId + "'";
    	
        Statement st = null;
    	try {
    		st = getStatement();
	        st.executeUpdate(sql);
	        return true;
	        
    	} catch (Exception e) {
    		LOG.error("delete user error", e);
    		return false;
    		
    	} finally {
			try {
				Connection conn = st.getConnection();
	    		st.close();
	    		conn.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    public int updateUserAlias(User newUser) {
        String sql = "UPDATE user set alias = ? where userId = ?";
        
        PreparedStatement st = null;
        try {
            st = getPreparedStatement(sql);
            st.setString(1, newUser.getAlias());
            st.setString(2, newUser.getUserId());
            return st.executeUpdate();
        } catch (SQLException e) {
            LOG.error("update user name error", e);
            return -1;
            
        } finally {
            try {
                Connection conn = st.getConnection();
                st.close();
                conn.close();
            } catch (SQLException e) {
            }            
        }
    }

	private User fromResultSetUser(ResultSet rs) throws SQLException {
		String userId = rs.getString("userId");
		String alias = rs.getString("alias");
		User user = new User(userId, alias);
		user.setCreateTime(rs.getDate("createTime"));
		return user;
	}

	private void init() throws SQLException {
		LOG.info("Begin to init H2 database.");

		try {
			Class.forName("org.h2.Driver");
		} catch (Exception e) {
			LOG.error("h2 database driver not found. Please ensure the driver jar is in classpath.");
			throw new SQLException("h2 database driver not found");
		}

		Statement st = null;
		try {
			_dataSource = createDriudDataSource();

			st = getStatement();
			Connection conn = st.getConnection();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getTables(null, null, null,
					new String[] { "TABLE" });
			ArrayList<String> tableList = new ArrayList<String>();
			while (rs.next()) {
				tableList.add(rs.getString("TABLE_NAME"));
			}
			LOG.debug("Exist tables - " + tableList.size() + " - "
					+ tableList.toString());

			if (!tableList.contains("USER")) {
				LOG.info("Create table - user");
				st.execute(SCRIPTS_CREATE_TABLE_USER);
			}

			if (!tableList.contains("SERVER_INFO")) {
				LOG.info("Create table - server_info");
				st.execute(SCRIPTS_CREATE_TABLE_SERVER_INFO);
			}

			String sql = "SELECT * from server_info";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				Configs.SERVER_ID = rs.getString(1);
			} else {
				String generated = "hellokitty";
				Configs.SERVER_ID = generated;
				sql = "INSERT INTO server_info (server_id) values ('"
						+ generated + "')";
				int result = st.executeUpdate(sql);
				if (result <= 0) {
					LOG.error("Unexcepiton: Insert server_id error.");
					throw new SQLException(
							"Unexcepiton: Insert server_id error.");
				}
			}
			LOG.info("The server_id: " + Configs.SERVER_ID);

		} catch (SQLException e) {
			LOG.error("Init error", e);
			e.printStackTrace();
			throw new SQLException("Init error");

		} finally {
			if (null == st)
				return;

			try {
				Connection conn = st.getConnection();
				st.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Statement getStatement() throws SQLException {
		Connection connection = _dataSource.getConnection();
		if (connection == null) {
			throw new SQLException(
					"Unexpected: cannot get connection from pool");
		}
		return connection.createStatement();
	}

	private PreparedStatement getPreparedStatement(String sql)
			throws SQLException {
		Connection connection = _dataSource.getConnection();
		if (connection == null) {
			throw new SQLException(
					"Unexpected: cannot get connection from pool");
		}
		return connection.prepareStatement(sql);
	}

	private DruidDataSource createDriudDataSource() {
		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:file:" + Configs.DB_PATH_FILE);
		dataSource.setUsername(Configs.DB_USERNAME);
		dataSource.setPassword(Configs.DB_PASSWORD);

		dataSource.setMaxActive(20);

		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxOpenPreparedStatements(50);

		dataSource.setTestWhileIdle(false);

		LOG.info("Datasource params - " + dataSource.getUsername() + ", "
				+ dataSource.getPassword() + ", " + dataSource.getUrl());

		return dataSource;
	}

}
