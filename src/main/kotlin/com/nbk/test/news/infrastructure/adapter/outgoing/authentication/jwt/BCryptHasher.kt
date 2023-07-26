package com.nbk.test.news.infrastructure.adapter.outgoing.authentication.jwt

import org.mindrot.jbcrypt.BCrypt

class BCryptHasher {

    companion object {
        fun encodePassword(password: String): String {
            return BCrypt.hashpw(password, BCrypt.gensalt(14))
        }
    }
}