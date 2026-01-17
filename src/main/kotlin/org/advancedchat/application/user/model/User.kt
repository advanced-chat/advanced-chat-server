package org.advancedchat.application.user.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.advancedchat.core.domain.model.DomainModel
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Entity
@Table(name = "users")
@SequenceGenerator(name = "users_id_generator", sequenceName = "users_id_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class User(var name: String, var displayName: String) : DomainModel() {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
  override var id: Long? = null
}
