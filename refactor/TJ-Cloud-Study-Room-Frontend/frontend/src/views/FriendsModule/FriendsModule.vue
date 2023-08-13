<template>
  <div>
    <!-- 悬浮球 -->
    <div v-drag class="floating_ball">
      <!-- 悬浮球的内容 -->
      <div class="ball_content" @click="showContent">
        <!-- 悬浮球的图标 -->
        <el-icon class="icon">
          <Connection/>
        </el-icon>
      </div>
      <!-- 好友信息窗口 -->
      <FriendsMenu v-if="this.isFriendShow.value" class="menu"/>
    </div>
  </div>
</template>

<script>
import FriendsMenu from "@/views/FriendsModule/FriendsMenu.vue";
import {ChatLineRound, Cloudy, Comment, Connection, PhoneFilled} from "@element-plus/icons-vue";

export default {
  components: {
    Connection,
    ChatLineRound,
    PhoneFilled,
    Comment,
    Cloudy,
    FriendsMenu,
  },
  inject: ['isNightMode', 'isFriendShow'],
  data() {
    return {};
  },
  methods: {
    //展示好友菜单
    showContent() {
      this.isFriendShow.value = !this.isFriendShow.value; // 点击悬浮球切换FloatMenu的显示和隐藏
    },

    //黑夜模式和白天模式的切换
    nightModeChanged() {
      if (this.isNightMode.value) {
        const floatingBall = document.querySelectorAll('.floating_ball');
        const ballContent = document.querySelectorAll('.ball_content');
        floatingBall.style.background = 'white';
        ballContent.style.color = '#1f2938';

      } else {
        const floatingBall = document.querySelectorAll('.floating_ball');
        const ballContent = document.querySelectorAll('.ball_content');
        floatingBall.style.background = 'linear-gradient(to right, #dde7f1, #6bc3f5)';
        ballContent.style.color = 'white';
      }
    },
  },
};
</script>

<style scoped>
/* 悬浮球样式 */
.floating_ball {
  position: absolute;
  bottom: 20px;
  right: 20px;
  z-index: 999;
  width: 55px;
  height: 55px;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
  box-shadow: 3px 3px 4px rgba(0, 0, 255, 0.3);
  background: linear-gradient(to right, #dde7f1, #6bc3f5);
}
.ball_content {
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}
.ball_content .icon {
  font-size: 30px;
}
.menu {
  position: absolute;
  bottom: 70px;
  right: 25px;
  z-index: 999;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  width: 350px;
  height: 622px;
  border-radius: 10px;
  overflow: hidden;
}
</style>
