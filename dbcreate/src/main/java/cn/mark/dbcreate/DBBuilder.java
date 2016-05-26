package cn.mark.dbcreate;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by yaoping on 2016/5/25.
 *
 */
public class DBBuilder {
    private Schema schema;
    private List<String> notNullStrProperties;
    private List<String> notNullIntProperties;
    private List<String> strProperties;
    private List<String> dateProperties;
    private List<String> booleanProperties;
    private List<String> intProperties;
    private String tableName;
    private boolean addedIdProperty;

    public DBBuilder(Schema schema) {
        this.schema = schema;
        initPropertyContainer();
    }

    private void initPropertyContainer() {
        notNullStrProperties = new ArrayList<>();
        notNullIntProperties = new ArrayList<>();
        strProperties = new ArrayList<>();
        dateProperties = new ArrayList<>();
        booleanProperties = new ArrayList<>();
        intProperties = new ArrayList<>();
    }

    public void clearPropertyContainer() {
        notNullStrProperties.clear();
        notNullIntProperties.clear();
        strProperties.clear();
        dateProperties.clear();
        booleanProperties.clear();
        intProperties.clear();
    }

    public DBBuilder prepareTable(String table) {
        clearPropertyContainer();
        tableName = table;
        addedIdProperty = false;
        return this;
    }

    public DBBuilder addIdProperty() {
        addedIdProperty = true;
        return this;
    }

    public DBBuilder addStringProperty(String property) {
        strProperties.add(property);
        return this;
    }

    public DBBuilder addStringPropertyNotNull(String property) {
        notNullStrProperties.add(property);
        return this;
    }

    public DBBuilder addIntProperty(String property) {
        intProperties.add(property);
        return this;
    }

    public DBBuilder addDateProperty(String property) {
        dateProperties.add(property);
        return this;
    }

    public DBBuilder addBooleanProperty(String property) {
        booleanProperties.add(property);
        return this;
    }

    public DBBuilder addIntPropertyNotNull(String property) {
        notNullIntProperties.add(property);
        return this;
    }

    public void build() {
        Entity entity = schema.addEntity(tableName);
        for (String item : notNullStrProperties) {
            entity.addStringProperty(item).notNull();
        }
        for (String item : notNullIntProperties) {
            entity.addIntProperty(item).notNull();
        }
        for (String item : strProperties) {
            entity.addStringProperty(item);
        }
        for (String item : dateProperties) {
            entity.addDateProperty(item);
        }
        for (String item : booleanProperties) {
            entity.addBooleanProperty(item);
        }
        for (String item : intProperties) {
            entity.addIntProperty(item);
        }
        if (addedIdProperty) {
            entity.addIdProperty();
        }
    }
}
