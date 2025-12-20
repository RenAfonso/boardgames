package com.codelayers.boardgames.auth.repository

import com.codelayers.boardgames.auth.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleEntity, Long> {

    fun findByName(name: String): RoleEntity?
}