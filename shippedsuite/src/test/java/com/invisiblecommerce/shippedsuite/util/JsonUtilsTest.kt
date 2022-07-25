package com.invisiblecommerce.shippedsuite.util

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JsonUtilsTest {

    @Test
    fun optIntTest() {
        val jsonObject = JSONObject().put("key", 1)
        assertEquals(1, JsonUtils.optInt(jsonObject, "key"))
    }

    @Test
    fun optBoolTest() {
        val jsonObject = JSONObject().put("key", true)
        assertTrue(JsonUtils.optBoolean(jsonObject, "key"))
    }

    @Test
    fun optDoubleTest() {
        val jsonObject = JSONObject().put("key", 1.01)
        assertEquals(1.01, JsonUtils.optDouble(jsonObject, "key"))
    }

    @Test
    fun optMapTest() {
        val jsonObject = JSONObject().put("key", mapOf(
            "a" to "a",
            "b" to "b",
            "c" to true,
            "d" to 123
        ))
        assertEquals(mapOf(
            "a" to "a",
            "b" to "b",
            "c" to true,
            "d" to 123
        ), JsonUtils.optMap(jsonObject, "key"))
    }

    @Test
    fun optStringTest() {
        val jsonObject = JSONObject().put("key", "value")
        assertEquals("value", JsonUtils.optString(jsonObject, "key"))

        jsonObject.put("key", "null")
        assertNull(JsonUtils.optString(jsonObject, "key"))

        jsonObject.put("key", "value")
        val ob = JsonUtils.optString(jsonObject, "nokeyshere")
        assertNull(ob)
    }

    @Test
    fun jsonObjectToMapTest() {
        assertNull(JsonUtils.jsonObjectToMap(null))

        val expectedMap = mapOf(
            "a" to "a",
            "b" to "b",
            "c" to true,
            "d" to 123
        )

        val mappedObject = JsonUtils.jsonObjectToMap(TEST_JSON_OBJECT)
        assertEquals(expectedMap, mappedObject)
    }

    @Test
    fun jsonObjectWithListToMapTest() {
        val expectedMap = mapOf(
            "a" to "a",
            "list" to mapOf(
                "b" to "b"
            )
        )

        val mappedObject = JsonUtils.jsonObjectToMap(TEST_JSON_OBJECT_WITH_LIST)
        assertEquals(expectedMap, mappedObject)
    }

    @Test
    fun jsonArrayToListTest() {
        assertNull(JsonUtils.jsonArrayToList(null))

        val expectedList = listOf("a", "b", "c", "d", true)
        val convertedJsonArray = JsonUtils.jsonArrayToList(TEST_JSON_ARRAY)
        assertEquals(expectedList, convertedJsonArray)
    }

    private companion object {

        private val TEST_JSON_ARRAY = JSONArray(
            """
            ["a", "b", "c", "d", true]
            """.trimIndent()
        )

        private val TEST_JSON_OBJECT = JSONObject(
            """
            {
                "a": "a",
                "b": "b",
                "c": true,
                "d": 123
            }
            """.trimIndent()
        )

        private val TEST_JSON_OBJECT_WITH_LIST = JSONObject(
            """
            {
                "a": "a",
                "list": {"b": "b"}
            }
            """.trimIndent()
        )
    }
}
