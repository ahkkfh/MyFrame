package cn.mark.dbcreate;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by yaoping on 2016/5/25.
 * 数据库创建字段文件等
 */
public class DBGenerator {

    public static void main(String[] args) {
        initDataFile();
    }

    private static void initDataFile() {
        DBConfig config = new DBConfig();
        Schema schema = new Schema(config.getDBVersion(), config.getSourceStructure());

        DBBuilder builder = new DBBuilder(schema);
        addUser(builder);
        try {
            new DaoGenerator().generateAll(schema, config.getSourceDir());
        } catch (Exception e) {

        }
    }

    private static void addUser(DBBuilder dbTableBuilder) {
        dbTableBuilder.prepareTable(DBConfig.TableName.User.toString())
                .addIdProperty()
                .addIntProperty(DBConfig.UserTable.user_id.toString())
                .addBooleanProperty(DBConfig.UserTable.isLogin.toString())
                .addStringPropertyNotNull(DBConfig.UserTable.username.toString())
                .addStringProperty(DBConfig.UserTable.nickname.toString())
                .addStringProperty(DBConfig.UserTable.image.toString())
                .addStringProperty(DBConfig.UserTable.phone.toString())
                .addStringProperty(DBConfig.UserTable.token.toString())
                .addIntProperty(DBConfig.UserTable.phone_verify.toString())
                .addStringProperty(DBConfig.UserTable.email.toString())
                .addStringProperty(DBConfig.UserTable.taobao_phone.toString())
                .addIntProperty(DBConfig.UserTable.taobao_phone_verify.toString())
                .build();
    }
}
