package eu.napcode.gonoteit.api;

import java.lang.reflect.InvocationTargetException;

import eu.napcode.gonoteit.type.ReadAccess;
import eu.napcode.gonoteit.type.Type;
import eu.napcode.gonoteit.type.WriteAccess;

public class ApiEntity {

    String uuid;
    Long id;
    Long updatedAt;
    Type type;
    ReadAccess readAccess;
    WriteAccess writeAccess;

    private static final String UUID_METHOD = "uuid";
    private static final String ID_METHOD = "id";
    private static final String TYPE_METHOD = "type";
    private static final String UPDATED_AT = "updatedAt";
    private static final String READ_PERMS_METHOD = "readAccess";
    private static final String WRITE_PERMS_METHOD = "writeAccess";

    public ApiEntity(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        uuid = o.getClass().getMethod(UUID_METHOD).invoke(o).toString();
        id = (Long) o.getClass().getMethod(ID_METHOD).invoke(o);
        updatedAt = (Long) o.getClass().getMethod(UPDATED_AT).invoke(o);
        type = (Type) o.getClass().getMethod(TYPE_METHOD).invoke(o);
        readAccess = (ReadAccess) o.getClass().getMethod(READ_PERMS_METHOD).invoke(o);
        writeAccess = (WriteAccess) o.getClass().getMethod(WRITE_PERMS_METHOD).invoke(o);
    }

    public String getUuid() {
        return uuid;
    }

    public Long getId() {
        return id;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Type getType() {
        return type;
    }

    public ReadAccess getReadAccess() {
        return readAccess;
    }

    public WriteAccess getWriteAccess() {
        return writeAccess;
    }
}
