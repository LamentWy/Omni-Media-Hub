<template>
  <a-config-provider
    :theme="{
      token: {
        colorPrimary: '#ffa500',
      },
      algorithm: theme.darkAlgorithm,
    }"
  >
    <a-button />
  </a-config-provider>
</template>

<script lang="ts" setup>
import { theme } from 'ant-design-vue'
</script>
