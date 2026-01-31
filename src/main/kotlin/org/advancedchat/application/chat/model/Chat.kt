package org.advancedchat.application.chat.model

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
@Table(name = "chats")
@SequenceGenerator(name = "chats_id_generator", sequenceName = "chats_id_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Chat(var name: String) : DomainModel() {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_generator")
  override var id: Long = 0
}
