<script setup lang="ts">
import {Chats, Layout} from '@advanced-chat/components'
import '@advanced-chat/components/styles'

const chats = [
  {
    "id": 1,
    "name": "Alice",
    "lastMessage": "Hey there!"
  },
  {
    "id": 2,
    "name": "Bob",
    "lastMessage": "What's up?"
  },
  {
    "id": 3,
    "name": "Charlie",
    "lastMessage": "See you soon."
  }
];

const addChat = () => {
  console.log('Add chat clicked');
};

const searchChat = (query: string) => {
  console.log('Search chat query:', query);
};
</script>

<template>
  <h1>You did it!</h1>
  <Layout :height="'600px'" :theme="'light'">
    <Chats
        :chats="chats"
        @add-chat="addChat"
        @search-chat="searchChat"
    />
  </Layout>
</template>

<style scoped></style>
