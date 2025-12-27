package com.codelayers.boardgames.exception

class UserNotFoundException(
    username: String
) : RuntimeException("User not found: $username")
