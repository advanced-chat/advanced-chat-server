package org.advancedchat.core.domain.repository

import org.advancedchat.core.domain.model.DomainModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean interface DomainRepository<T : DomainModel> : JpaRepository<T, Long>
