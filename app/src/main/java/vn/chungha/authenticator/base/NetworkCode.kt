package vn.chungha.authenticator.base

enum class NetworkCode(val code: Int) {
    SERVER_ERROR(500),
    FORBIDDEN_ERROR(403),
    BAD_REQUEST_ERROR(400),
    NOT_FOUND_ERROR(404),
    UNAUTHORIZED_ERROR(401),
    SERVER_SUCCESS(200)
}