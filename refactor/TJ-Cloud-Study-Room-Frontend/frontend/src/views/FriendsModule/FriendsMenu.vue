<template>
  <div class="window-wrap">
    <!-- 显示左侧边栏 -->
    <div class="sidebar-wrap">
      <BarLeft @change-content="changeContent" />
    </div>
    <!-- 显示右侧内容 -->
    <div class="content-wrap">
      <div class="content">
        <template v-if="activeContent === 'user'">
          <!-- 显示好友列表 -->
          <FriendList @friend-selected="handleFriendSelected" />
        </template>
        <template v-else-if="activeContent === 'chat'">
          <!-- 显示聊天内容 -->
          <Chat :friend="selectedFriend" />
        </template>
        <template v-else-if="activeContent === 'notifications'">
          <!-- 显示通知内容 -->
          <Notification />
        </template>
        <template v-else-if="activeContent === 'recommend'">
          <!-- 显示推荐内容 -->
          <Recommend />
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref } from 'vue'
import BarLeft from '@/components/FriendsModule/sidebar-left'
import FriendList from "@/components/FriendsModule/friendList";
import Chat from "@/components/FriendsModule/chat";
import Notification from "@/components/FriendsModule/notification";
import Recommend from "@/components/FriendsModule/recommend";
export default defineComponent({
  components: {
    BarLeft,
    Chat,
    FriendList,
    Notification,
    Recommend
  },
  setup() {
    // 使用 ref 函数定义响应式变量
    const activeContent = ref('user'); // 初始化 activeContent 为 'friendList'
    const selectedFriend = ref(null); // 初始化 selectedFriend 变量为 null

    // 定义用于改变当前显示内容的函数
    const changeContent = (content) => {
      activeContent.value = content; // 更新 activeContent 为传入的 content 参数
    };

    // 定义处理选择好友事件的函数
    const handleFriendSelected = (friend) => {
      activeContent.value = 'chat'; // 切换到聊天内容，将 activeContent 设置为 'chat'
      selectedFriend.value = friend; // 更新 selectedFriend 变量为选中的好友
    };

    // 将需要访问的变量和函数通过返回对象暴露出去
    return {
      activeContent, // 将 activeContent 变量暴露出去
      selectedFriend, // 将 selectedFriend 变量暴露出去
      changeContent, // 将 changeContent 函数暴露出去
      handleFriendSelected, // 将 handleFriendSelected 函数暴露出去
    };
  }
})
</script>

<style scoped>
.window-wrap {
  width: 100%; /* 适应父容器的宽度 */
  height: 100%; /* 适应父容器的高度 */
  display: flex;
  position: relative; /* 相对定位 */
}
.sidebar-wrap {
  width: 60px;
}
.content-wrap {
  width: 290px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  height: 100%; /* 使内容区域撑满高度 */
}
.content {
  flex-grow: 1;
  /*白天模式*/
  background-color: rgba(243, 250, 253, 0.95);
  padding: 0;
}
</style>



