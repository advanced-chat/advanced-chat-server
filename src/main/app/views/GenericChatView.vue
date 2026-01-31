<script setup lang="ts">
import { Layout, Chats, Chat, MediaPreview } from '@advanced-chat/components'
import '@advanced-chat/components/styles'
</script>

<template>
  <Layout height="600px" theme="auto">
    <div class="vac-chat-container">
      <Chats />

      <Chat />
    </div>
    <transition name="vac-fade-preview" appear>
      <MediaPreview />
    </transition>
  </Layout>
</template>

<style scoped lang="scss">
.vac-chat-container {
  height: 100%;
  display: flex;

  input {
    min-width: 10px;
  }

  textarea,
  input[type='text'],
  input[type='search'] {
    -webkit-appearance: none;
  }
}
</style>
