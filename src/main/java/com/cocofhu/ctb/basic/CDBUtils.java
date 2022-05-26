package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CDBUtils {

    @CExecutorOutput(info = "mul result",type = Connection.class, name = CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY)
    @CExecBasicInfo(name="AcquireConnection",info = "info", group = "test")
    public Connection acquireConnection(
            @CExecutorInput String driverName,
            @CExecutorInput String url,
            @CExecutorInput String username,
            @CExecutorInput String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        return DriverManager.getConnection(url, username, password);
    }

    @CExecutorOutput(info = "mul result",type = Map.class, name = CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY)
    @CExecBasicInfo(name="QueryAsMap",info = "info", group = "test")
    public List<Map<String, Object>> queryAsMap(@CExecutorInput Connection connection, @CExecutorInput String sql) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.query(connection,sql,new MapListHandler());
    }


    @CExecutorOutput(info = "",type = String.class, name = "url")
    @CExecutorOutput(info = "",type = String.class, name = "password")
    @CExecBasicInfo(name="ReadMySQLURLAndPassword",info = "info", group = "test")
    public void ReadMySQLURLAndPassword(@CExecutorInput String source,@CAutowired CDefaultExecutionRuntime runtime) throws IOException {
        Properties pros =  new Properties();
        //读取配置文件的方式二：使用ClassLoader
        //配置文件默认识别为：当前module的src下
        ClassLoader classLoader = CDBUtils.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(source);
        pros.load(is);
        String url = pros.getProperty("url");
        String password = pros.getProperty("password");
        runtime.getCurrentLayer().put("url",url);
        runtime.getCurrentLayer().put("password",password);
        is.close();
    }

}
