<template>
  <div>
    <!-- 如果有选中的好友，则显示聊天容器 -->
    <div v-if="friend" class="chat-container">
      <!-- 聊天导航栏，显示聊天好友的头像和用户名 -->
      <div class="chat-header">
        <!-- 好友头像 -->
        <div class="friend-header">
          <el-avatar :size="40" :src="friend.avatar"></el-avatar>
        </div>
        <!-- 好友用户名 -->
        <div class="friend-name">{{ friendName }}</div>
      </div>

      <!-- 消息列表区域 -->
      <div ref="messageList" class="message-list">
        <!-- 遍历消息列表 -->
        <div v-for="message in messages" :key="message.id" class="message-card">
          <!-- 头像显示区域 -->
          <div
              :class="{'self-avatar': message.fromUser === selfName, 'friend-avatar': message.fromUser === this.friend.name}"
              class="message-avatar">
            <!-- 根据消息的发送者显示不同的头像 -->
            <el-avatar :size="30" :src="message.fromUser === this.friend.name ? friend.avatar : selfAvatar"></el-avatar>
          </div>

          <!-- 消息内容显示区域 -->
          <div
              :class="{'self-message': message.fromUser === selfName, 'friend-message': message.fromUser === this.friend.name}"
              class="message-content">
            <!-- 显示消息文本内容 -->
            {{ message.text }}
          </div>
        </div>
      </div>

      <!-- 发送消息区域 -->
      <div class="message-input">
        <!-- 输入框，绑定输入内容到 newMessage 变量 -->
        <el-input
            v-model="newMessage"
            clearable
            placeholder="请输入消息"
            @clear="clearMessage"
            @keydown.enter="sendMessage"
        ></el-input>

        <!-- 表情按钮，点击显示/隐藏表情面板 -->
        <el-button type="text" @click="showEmojis = !showEmojis">
          <img class="emoji-icon" src="@/assets/emoji.png">
        </el-button>

        <!-- 发送按钮 -->
        <el-button class="send-button" type="text" @click="sendMessage">
          <el-icon size="15">
            <Promotion/>
          </el-icon>
        </el-button>

        <!-- 表情面板 -->
        <div v-if="showEmojis" class="emoji-panel">
          <!-- 遍历显示可选的表情 -->
          <span v-for="emoji in emojis" :key="emoji" class="emoji" @click="addEmoji(emoji)">{{ emoji }}</span>
        </div>
      </div>
    </div>

    <!-- 如果没有选中的好友，则显示提示 -->
    <div v-else class="friend-not-selected">
      <!-- 顶部空白占位 -->
      <div class="null-header"></div>

      <!-- 背景图片 -->
      <div :style="{ 'background-image': getRandomBackgroundImage() }" class="background-image"></div>

      <!-- 提示消息 -->
      <div class="prompt-message">快去找好友聊天吧~</div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue';
import {Promotion} from "@element-plus/icons-vue";
import background1 from '@/assets/1.png';
import background2 from '@/assets/2.png';
import emojis from '@/emoji';
import request from "@/utils/request";

let socket;
export default defineComponent({
  name: 'Chat',
  components: {Promotion},
  props: ['friend'],
  computed: {
    //好友用户名
    friendName() {
      return this.friend ? this.friend.name : '';
    },
    //自己头像
    selfAvatar() {
      let userJson = sessionStorage.getItem("user");
      const temporaryUser = JSON.parse(userJson);
      // 自己的头像，根据需要设置
      return temporaryUser.picture;//标记修改
    },
    //自己用户名
    selfName() {
      let userJson = sessionStorage.getItem("user");
      const temporaryUser = JSON.parse(userJson);
      // 自己的头像，根据需要设置
      return temporaryUser.userName;//标记修改
    }

  },

  data() {
    return {
      messages: [],
      emojis: emojis,
      selectedEmoji: '',
      newMessage: '',
      showEmojis: false,
    };
  },

  // 在created钩子函数中调用load_messages()方法导入历史聊天信息
  created() {
    this.initWebSocket();
    this.load_messages();
    this.scrollToBottom();
  },

  //mounted钩子
  mounted() {
    this.scrollToBottom();
  },
  methods: {
    //滚动条滑倒底端
    scrollToBottom() {
      if (this.friend) {
        this.$nextTick(() => {
          const messageList = this.$refs.messageList;
          messageList.scrollTop = messageList.scrollHeight;
        });
      }
    },

    //加载历史消息
    load_messages() {
      if (this.friend) {
        request
            .get('/api/getMessage', {
              params: {
                uname: temporaryUser.userName,
                fname: this.friend.name
              }
            })
            .then(res => {
              if (res.data.length != 0) {
                this.messages = res.data;
              } else {
                this.messages = [];
              }
            })
            .catch(error => {
              console.error(error);
            });
      }
    },

    //初始化WebSocket
    initWebSocket() {
      if (this.friend) {
        if (typeof WebSocket === 'undefined') {
          console.log("您的浏览器不支持WebSocket,请使用支持WebSocket的浏览器");
          return;
        }

        // 本地
        socket = new WebSocket("ws://localhost:9091/friendChat/" + temporaryUser.userName); //用你的WebSocket服务器URL替换
        // 云端
        // socket = new WebSocket("ws://121.37.184.56:80/websocketF/friendChat/" + temporaryUser.userName); //用你的WebSocket服务器URL替换
        socket.onopen = () => {
          console.log('WebSocket连接建立');
        };

        socket.onmessage = (event) => {
          let data = JSON.parse(event.data);
          if (data.fromUser === this.friend.name) {
            this.messages.push(data);
          }
          // 在收到新消息后调用 scrollToBottom() 方法
          this.scrollToBottom();
        };

        socket.onclose = () => {
          console.log('WebSocket connection closed.');
        };

        socket.onerror = (error) => {
          console.error('WebSocket error:', error);
        };
      }
    },

    //发送消息
    sendMessage() {
      if (!this.newMessage) {
        return;
      }
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        // 组装待发送的消息 json
        // {"from": "zhang", "to": "admin", "text": "聊天文本"}
        let userJson = sessionStorage.getItem("user");
        const temporaryUser = JSON.parse(userJson);
        let message = {fromUser: temporaryUser.userName, toUser: this.friend.name, text: this.newMessage}
        socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
        this.messages.push(message);
        this.newMessage = '';
        this.showEmojis = false; // 发送消息后隐藏表情选择面板

        console.log(message)

        request.post("/api/sendMessage", message).then(res => {
        }).catch(error => {
          console.error(error);
        });
      }
      this.scrollToBottom();
    },

    //添加表情
    addEmoji(emoji) {
      this.newMessage += emoji;
    },

    //清空消息
    clearMessage() {
      this.newMessage = '';
    },

    //获得随机的背景图片
    getRandomBackgroundImage() {
      const backgrounds = [background1, background2]; // Add more background images as needed
      const randomIndex = Math.floor(Math.random() * backgrounds.length);
      return `url(${backgrounds[randomIndex]})`;
    },

  },
});
</script>

<style scoped>
.chat-container {
  width: 290px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-header {
  width: 290px;
  height: 60px;
  display: flex;
  justify-content: flex-end; /* 将内容右对齐 */
  align-items: center;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.4);
  /*background-color: rgba(21, 159, 255, 0.1);*/
}

.friend-name {
  margin-left: 10px; /* 调整用户名与头像之间的间距 */
  font-weight: bold;
}

.friend-header {
  margin-right: 0;
}

.message-list {
  height: 502px; /* 设置最大高度 */
  overflow-y: auto;
  padding: 10px;
  flex-grow: 1;
}

.message-list::-webkit-scrollbar {
  /*width: 5px; !* 设置滚动条的宽度 *!*/
  width: 5px;
  background: transparent;
}

.message-list::-webkit-scrollbar-track {
  background-color: #f1f1f1; /* 设置滚动条轨道的背景色 */
}

.message-list::-webkit-scrollbar-thumb {
  background-color: rgba(243, 250, 253, 0.95); /* 设置滚动条滑块的背景色 */
  border-radius: 4px; /* 设置滚动条滑块的圆角 */
}

.message-list::-webkit-scrollbar-thumb:hover {
  background-color: #e3f2fd; /* 设置鼠标悬停时滚动条滑块的背景色 */
}

.message-card {
  position: relative; /* 添加相对定位 */
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
}

.message-avatar {
  position: absolute; /* 使用绝对定位 */
  top: 50%;
  transform: translateY(-50%);
}

.self-avatar {
  right: 0; /* 将自己的头像固定在最右侧 */
  margin-left: 10px;
}

.friend-avatar {
  left: 0; /* 将好友的头像固定在最左侧 */
  margin-right: 10px;
}

.friend-message {
  background-color: #e1f5fe;
  width: 85%; /* 设置好友消息的宽度为85% */
}

.self-message {
  background-color: #e3f2fd;
  width: 85%; /* 设置自己消息的宽度为85% */
}

.message-content {
  font-size: 14px;
  word-break: break-all;
  margin-right: 30px;
  margin-left: 30px;
  border-radius: 10px;
  padding: 10px;
}

.message-input {
  width: 290px;
  height: 60px;
  bottom: 0;
  position: absolute;
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.4);
  /*background-color: rgba(21, 159, 255, 0.1);*/
}

.message-input el-input {
  flex-grow: 1;
  margin-right: 10px;
}

.emoji-panel {
  position: absolute;
  bottom: 62px;
  right: 0;
  width: 150px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px;
  display: flex;
  flex-wrap: wrap;
  z-index: 1;
  height: 150px; /* 设置最大高度 */
  overflow-y: auto;
}

.emoji {
  cursor: pointer;
  margin: 2px;
  font-size: 18px;
}

.emoji-icon {
  margin-left: 10px;
  width: 15px;
  height: 15px;
}

.friend-not-selected {
  width: 290px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.null-header {
  width: 290px;
  height: 60px;
  display: flex;
  justify-content: flex-end; /* 将内容右对齐 */
  align-items: center;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.4);
  /*background-color: rgba(21, 159, 255, 0.1);*/
}

.background-image {
  width: 75%;
  height: 450px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  margin-right: 20px;
}

.prompt-message {
  margin-top: -100px;
  font-size: 15px;
  color: rgba(128, 115, 115, 0.76);
}
</style>


