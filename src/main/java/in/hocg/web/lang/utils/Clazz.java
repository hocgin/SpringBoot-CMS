package in.hocg.web.lang.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * (๑`灬´๑)
 * Created by hocgin on 十一月28  028.
 * 类 | 反射 相关的工具方法
 */
public class Clazz {

    /**
     * 获取对象内的属性
     * @param object
     * @param filedName
     * @return
     */
    public Field getField(Object object, String filedName) {
            Class<?> clazz = object.getClass();
            while (Object.class != clazz) {
                try {
                    Field field = clazz.getDeclaredField(filedName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException ignored) {
                }
                clazz = clazz.getSuperclass();
            }
            return null;
    }


    /**
     * 设置对象内的属性
     * @param object
     * @param filedName
     * @param value
     * @param <T>
     * @return
     */
    public <T> T set(T object, String filedName, Object value) {
        Field field = getField(object, filedName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
    
    /**
     * 获取泛型类型
     * @param clazz 待获取泛型类型的类
     * @param index 下标
     * @return
     */
    public static Class getTypeParam(Class clazz, int index) {
        ParameterizedType superclass = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class)(superclass.getActualTypeArguments()[index]);
    }
    
    public static boolean exitsClass(String clazzName) {
        Class<?> aClass;
        try {
            aClass = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return aClass != null;
    }
}
