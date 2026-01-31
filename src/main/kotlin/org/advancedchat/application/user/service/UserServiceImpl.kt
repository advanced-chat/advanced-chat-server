package org.advancedchat.application.user.service

import org.advancedchat.application.user.model.User
import org.advancedchat.application.user.repository.UserRepository
import org.advancedchat.core.domain.service.DomainService

@DomainService
class UserServiceImpl(private val repository: UserRepository) : UserService {
  override fun createUser(request: CreateUserRequest): User =
      with(request) { repository.save(User(name, displayName)) }

  override fun retrieveUser(name: String): User? = repository.findUserByName(name)
}
