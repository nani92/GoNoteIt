package eu.napcode.gonoteit.api;

import java.lang.reflect.InvocationTargetException;

import eu.napcode.gonoteit.type.Type;

public class ApiEntity {

    String uuid;
    Long id;
    Long updatedAt;
    Type type;

    private static final String UUID_METHOD = "uuid";
    private static final String ID_METHOD = "id";
    private static final String TYPE_METHOD = "type";
    private static final String UPDATED_AT = "updatedAt";

    public ApiEntity(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        uuid = o.getClass().getMethod(UUID_METHOD).invoke(o).toString();
        id = (Long) o.getClass().getMethod(ID_METHOD).invoke(o);
        updatedAt = (Long) o.getClass().getMethod(UPDATED_AT).invoke(o);
        type = (Type) o.getClass().getMethod(TYPE_METHOD).invoke(o);
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
}
