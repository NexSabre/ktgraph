import helpers.isTgResponseError
import khttp.delete
import khttp.post
import khttp.put
import org.json.JSONObject


class TigerGraphConfiguration(
    val tgInstanceAddress: String,
    val tgInstancePort: Int = 9000,
    val tgSecret: String = "",
    val tgGraphName: String? = null,
)

class TigerGraph(private val configuration: TigerGraphConfiguration) {
    var token: String?
    var expiration: Int?

    init {
        val instanceURI = "${configuration.tgInstanceAddress}:${configuration.tgInstancePort}/requesttoken"
        val payload = JSONObject(mapOf("secret" to configuration.tgSecret))
        if (configuration.tgGraphName != null) {
            payload.append("graph", configuration.tgGraphName)
        }

        val response = post(
            instanceURI,
            data = payload
        ).jsonObject
        isTgResponseError(response)

        token = response.get("token") as String
        expiration = response.get("expiration") as Int
    }

    fun refreshToken() {
        val instanceURI = "${configuration.tgInstanceAddress}:${configuration.tgInstancePort}/requesttoken"
        val payload = JSONObject(mapOf("secret" to configuration.tgSecret, "token" to token))

        val response = put(
            instanceURI,
            data = payload
        ).jsonObject
        isTgResponseError(response)

        token = response.get("token") as String
        expiration = response.get("expiration") as Int
    }

    fun dropToken() {
        val instanceURI = "${configuration.tgInstanceAddress}:${configuration.tgInstancePort}/requesttoken"
        val payload = JSONObject(mapOf("secret" to configuration.tgSecret, "token" to token))
        val response = delete(
            instanceURI,
            data = payload
        ).jsonObject
        isTgResponseError(response)

        token = null
        expiration = null
    }
}