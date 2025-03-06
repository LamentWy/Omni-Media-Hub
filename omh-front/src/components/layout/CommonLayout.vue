<template>
  <div id="common-layout">
    <a-layout class="layout">
      <!-- header -->
      <a-layout-header class="common-layout-a-header">
        <OmhHeader />
      </a-layout-header>
      <!-- content -->
      <a-layout-content class="common-layout-a-content">
        <RouterView />
      </a-layout-content>
      <!-- footer -->
      <a-layout-footer class="common-layout-a-footer">
        <OmhFooter />
      </a-layout-footer>
    </a-layout>
  </div>
</template>
<script lang="ts" setup></script>
<style scoped>
#common-layout .common-layout-a-header {
  top: 0;
  left: 0;
  right: 0;
  background-color: #b2dfdb;
  box-shadow: 0 2px 4px #8bc34a;
  box-sizing: border-box;
  position: fixed;
  z-index: 1030;
  height: 48px;
}

#common-layout .common-layout-a-footer {
  background-color: #b2dfdb;

  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  min-width: 100vw;
  text-align: center;
  padding: 2px;
  height: 24px;
}

#common-layout .common-layout-a-content {
  margin-top: 60px;
  margin-bottom: 40px;
  /* height: calc(100% - 104px); */
  max-height: 80%;
  padding: 0 50px;
}
</style>
