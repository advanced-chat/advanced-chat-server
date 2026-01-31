package org.advancedchat.application.user.repository

import org.advancedchat.application.user.model.User
import org.advancedchat.core.domain.repository.DomainRepository

interface UserRepository : DomainRepository<User> {
  fun findUserByName(name: String): User?
}
