package com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class JwtUtil(@Value("\${jwt.secret}") private val secret: String) {

    fun generateToken(email: String): String {

        val claims = JWTClaimsSet.Builder()
            .subject(email)
            .issueTime(Date())
            .expirationTime(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION_TIME)))
            .build()

        val signedJWT = SignedJWT(
            com.nimbusds.jose.JWSHeader(JWSAlgorithm.HS256),
            claims
        )

        val signer = MACSigner(secret)
        signedJWT.sign(signer)

        logger.info("\n - - - - [GENERATED TOKEN] - - - - \n")
        return signedJWT.serialize()
    }

    fun extractSubject(token: String): String? {
        try {
            val signedJWT = SignedJWT.parse(token)
            return signedJWT.jwtClaimsSet.subject
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("\n- - - - [MALFORMED TOKEN] - - - - \n")
        }
        return null
    }


    fun isTokenValid(token: String): Boolean {
        return !isTokenExpired(token) && isSignatureValid(token)
    }

    private fun isSignatureValid(token: String): Boolean {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val verifier = MACVerifier(secret)
            signedJWT.verify(verifier)
        } catch (e: Exception) {
            logger.error("\n- - - - [INVALID TOKEN SIGNATURE] - - - - \n")
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val expirationTime = signedJWT.jwtClaimsSet.expirationTime.time
            val currentTime = Date().time
            expirationTime < currentTime
        } catch (e: Exception) {
            logger.error("\n- - - - [EXPIRED TOKEN] - - - - \n")
            true
        }
    }

    companion object {
        private const val ONE_HOUR: Long = (60 * 60)
        const val JWT_EXPIRATION_TIME = ONE_HOUR
        private val logger = LoggerFactory.getLogger(JwtUtil::class.java)
    }
}