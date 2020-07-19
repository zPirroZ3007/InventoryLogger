package me.pirro.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	public static void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
		setPrivateField(instance.getClass(), instance, fieldName, value);
	}

	public static void setPrivateField(Class<?> clazz, Object instance, String fieldName, Object newValue) throws Exception {
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		f.set(instance, newValue);
	}

	public static Object getPrivateField(Object instance, String fieldName) throws Exception {
		return getPrivateField(instance.getClass(), instance, fieldName);
	}

	public static Object getPrivateField(Class<?> clazz, Object instance, String fieldName) throws Exception {
		Field f = clazz.getDeclaredField(fieldName);
		f.setAccessible(true);
		return f.get(instance);
	}

	public static void setPrivateFinalField(Object instance, Field field, Object newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(instance, newValue);
	}

}