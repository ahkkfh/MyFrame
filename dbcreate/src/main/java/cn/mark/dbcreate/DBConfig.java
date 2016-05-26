package cn.mark.dbcreate;

/**
 * Created by yaoping on 2016/5/25.
 */
public class DBConfig {
    private String sourceDir = "../MyFrame/network/src/main/java";
    private int DBVersion = 1;
    private String sourceStructure = "cn.mark.network.db";

    public enum TableName {
        User
    }

    public enum UserTable {
        user_id,
        isLogin,
        username,
        nickname,
        image,
        phone,
        phone_verify,
        email,
        taobao_phone,
        taobao_phone_verify,
        token
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public int getDBVersion() {
        return DBVersion;
    }

    public String getSourceStructure() {
        return sourceStructure;
    }
}
