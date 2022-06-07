package helpers

import org.json.JSONObject

fun isTgResponseError(response: JSONObject) {
    if (response.getBoolean("error")) {
        throw Exception("TigerGraph Error: ${response.get("code")} - ${response.get("message")}")
    }
}