package com.lqk.net

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType


object ResponseEntityToModule {
    fun parseJsonToModule(jsonContent: String, clazz: Class<*>): Any? {
        var moduleObj: Any? = null
        try {
            val jsonObj = JSONObject(jsonContent)
            moduleObj = parseJsonObjectToModule(jsonObj, clazz)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return moduleObj
    }

    fun parseJsonObjectToModule(jsonObj: JSONObject, clazz: Class<*>): Class<*>? {
        var moduleObj: Class<*>? = null
        try {
            moduleObj = clazz.newInstance() as Class<*>
            setFieldValue(moduleObj, jsonObj, clazz)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return moduleObj
    }

    @Throws(IllegalArgumentException::class, IllegalAccessException::class, JSONException::class, InstantiationException::class)
    private fun setFieldValue(moduleObj: Any?, jsonObj: JSONObject, clazz: Class<*>) {
        if (clazz.superclass != null) {
            setFieldValue(moduleObj, jsonObj, clazz.superclass!!)
        }
        val fields = clazz.declaredFields
        var cls: Class<*>
        var name: String
        for (f in fields) {
            f.isAccessible = true
            cls = f.type
            name = f.name
            if (!jsonObj.has(name) || jsonObj.isNull(name)) {
                continue
            }
            if (cls.isPrimitive || isWrappedPrimitive(cls)) {
                setPrimitiveFieldValue(f, moduleObj, jsonObj.get(name))
            } else {
                when {
                    cls.isAssignableFrom(String::class.java) -> {
                        f.set(moduleObj, jsonObj.get(name).toString())
                    }
                    cls.isAssignableFrom(ArrayList::class.java) -> {
                        parseJsonArrayToList(f, name, moduleObj, jsonObj)
                    }
                    else -> {
                        val obj = parseJsonObjectToModule(jsonObj.getJSONObject(name), cls.newInstance().javaClass)
                        f.set(moduleObj, obj)
                    }
                }
            }
        }
    }

    @Throws(JSONException::class, IllegalArgumentException::class, IllegalAccessException::class)
    private fun parseJsonArrayToList(field: Field, fieldName: String,
                                     moduleObj: Any?, jsonObj: JSONObject): ArrayList<Any> {
        val objList = ArrayList<Any>()
        val fc = field.getGenericType()
        if (fc is ParameterizedType)
        // 锟角凤拷锟斤拷
        {
            val pt = fc as ParameterizedType
            if (pt.actualTypeArguments[0] is Class<*>)
            // 锟斤拷指锟斤拷锟斤拷锟斤拷,锟斤拷锟斤拷"?"
            {
                val clss = pt.actualTypeArguments[0] as Class<*>

                if (jsonObj.get(fieldName) is JSONArray) {
                    val array = jsonObj.getJSONArray(fieldName)
                    for (i in 0 until array.length()) {
                        if (array.get(i) is JSONObject) {
                            if (parseJsonObjectToModule(array.getJSONObject(i), clss) != null) {
                                objList.add(parseJsonObjectToModule(array.getJSONObject(i), clss)!!)
                            }
                        } else {
                            if (clss.isAssignableFrom(array.get(i).javaClass)) {
                                objList.add(array.get(i))
                            }
                        }
                    }
                }
                field.set(moduleObj, objList)
            }
        }
        return objList
    }

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    private fun setPrimitiveFieldValue(field: Field, moduleObj: Any?, jsonObj: Any) {
        if (field.type.isAssignableFrom(jsonObj.javaClass)) {
            field.set(moduleObj, jsonObj)
        } else {
            field.set(moduleObj, makeTypeSafeValue(field.type, jsonObj.toString()))
        }
    }

    @Throws(NumberFormatException::class, IllegalArgumentException::class)
    private fun makeTypeSafeValue(type: Class<*>, value: String): Any? {
        return if (Int::class.javaPrimitiveType == type || Int::class.java == type) {
            Integer.parseInt(value)
        } else if (Long::class.javaPrimitiveType == type || Long::class.java == type) {
            java.lang.Long.parseLong(value)
        } else if (Short::class.javaPrimitiveType == type || Short::class.java == type) {
            java.lang.Short.parseShort(value)
        } else if (Char::class.javaPrimitiveType == type || Char::class.java == type) {
            value[0]
        } else if (Byte::class.javaPrimitiveType == type || Byte::class.java == type) {
            java.lang.Byte.valueOf(value)
        } else if (Float::class.javaPrimitiveType == type || Float::class.java == type) {
            java.lang.Float.parseFloat(value)
        } else if (Double::class.javaPrimitiveType == type || Double::class.java == type) {
            java.lang.Double.parseDouble(value)
        } else if (Boolean::class.javaPrimitiveType == type || Boolean::class.java == type) {
            java.lang.Boolean.valueOf(value)
        } else {
            value
        }
    }

    private fun isWrappedPrimitive(type: Class<*>): Boolean {
        return (type.name == Boolean::class.java.name
                || type.name == Byte::class.java.name
                || type.name == Char::class.java.name
                || type.name == Short::class.java.name
                || type.name == Int::class.java.name
                || type.name == Long::class.java.name
                || type.name == Float::class.java.name
                || type.name == Double::class.java.name)
    }
}
