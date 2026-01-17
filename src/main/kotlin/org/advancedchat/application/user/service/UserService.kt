package org.advancedchat.application.user.service

import org.advancedchat.application.user.model.User

interface UserService {
  fun createUser(request: CreateUserRequest): User

  fun readUser(name: String): User?
}
