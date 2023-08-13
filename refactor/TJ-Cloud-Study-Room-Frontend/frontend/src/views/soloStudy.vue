<template xmlns:el-card="http://www.w3.org/1999/html">
    <Header />
    <FriendsModule />
    <div style="display: flex" >
        <Aside />
        <div class="container">
            <el-card class="box-card" :style="{ backgroundImage: 'url(' + backgroundImageUrl + ')' }" shadow="always">
<!--                第一行用于展示选项按钮-->
                <el-row class="button-row" justify="space-evenly">
<!--                    Personal Time-->
                    <el-col :span="6">
                        <div class="flex">
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Time"
                                placement="bottom"
                            ><el-button class="common-button" color="black" @click="showCard(3)" :icon="Timer"> Personal Time</el-button>
                            </el-tooltip>
                        </div><div class="grid-content ep-bg-purple" /></el-col>

                    <el-col :span="18" class="right-button">
                        <div class="flex">
                            <!--Backgroud-->
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Background"
                                placement="bottom"
                            >
                                <el-button class="common-button" type="primary" color="black" size="large" @click="showCard(0)" :icon="Picture" />
                            </el-tooltip>
                            <!--Sound-->
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Sound"
                                placement="bottom"
                            >
                                <el-button class="common-button" type="primary" color="black" size="large" @click="showCard(1)" :icon="Headset" />
                            </el-tooltip>
                            <!--Quoto-->
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Motivational Quote"
                                placement="bottom"
                            >
                                <el-button class="common-button" type="primary" color="black" @click="showCard(2)" :icon="EditPen"/>
                            </el-tooltip>
                            <!--Full Screen-->
                            <el-tooltip
                                class="box-item"
                                effect="dark"
                                content="Full Screen"
                                placement="bottom"
                            >
                                <el-button class="common-button" type="primary" color="black" size="large"  @click="simulateF11" :icon="FullScreen" />
                            </el-tooltip>
                        </div>
                        <div class="grid-content ep-bg-purple" /></el-col>
                </el-row>
<!--                第二行用于展示选项具体内容-->
                <el-row class="content-row" justify="space-evenly">
                    <el-col  :span="12">
                        <div class="pop-card">
                            <el-card class="time-card" v-if="cardVisible[3]" :style="{ backgroundColor: '#000' }">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Personal Time</span>
                                    <div style="display: flex">
                                        <el-button :icon="Close" type="primary" color="black" size="small" class="close-button" @click="closeCard(3)" circle />
                                    </div>
                                </div>
                                <el-row justify="space-evenly" v-if="rowVisible" align="middle" style="margin-top: 5px">
                                    <el-col :span="12">
                                        <div style="color: #fff; font-size: 24px; text-align: left" >{{ formatTime }}
                                        </div><div/>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px;" @click="togglePause">
                                            <VideoPause />
                                        </el-icon>
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px; margin-left: 30px;" @click="showRow">
                                            <RefreshLeft />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                </el-row>

                                <el-row justify="space-evenly" v-if="!rowVisible" align="middle" style="margin-top: 5px">
                                    <el-col :span="6">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px;" @click="decrementDuration">
                                            <Minus />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                    <el-col :span="12">
                                        <div style="color: #fff; font-size: 24px; text-align: left; margin-left: 20px;" >{{ initTime }}
                                        </div>
                                        <div/>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-icon :size="30" class="myicon" color="white" style="cursor: pointer; padding-top: 12px; margin-left: 40px;" @click="incrementDuration">
                                            <Plus />
                                        </el-icon>
                                        <div/>
                                    </el-col>
                                </el-row>
                                <el-row align="middle" v-if="!rowVisible" style="margin-top: 5px" justify="center">
                                    <el-button type="info" plain  @click="closeRow">Start timer</el-button>
                                </el-row>
                            </el-card>
                        </div></el-col>

                    <el-col  :span="12" class="right-pop-card">
                        <div class="pop-card" justify="end">
                            <el-card class="common-card" v-if="cardVisible[0]" :style="{ backgroundColor: '#000',zIndex: 9999}">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Background</span>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" class="nightbutton" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard(0)" circle />
                                    </div>
                                </div>
                                <div style="color: #fff">
                                    <el-row :gutter="18" class="pic-row">
                                        <el-col v-for="(url, index) in urllist" :key="index" :span="8" class="image-container" @click="changeBackground(index)">
                                            <el-image class="image-clickable" style="width: 65px; height: 65px; border-radius: 10px;"
                                                      :src="url" :preview-src-list="srcList">
                                            </el-image>
                                            <div></div>
                                        </el-col>
                                    </el-row>
                                </div>
                            </el-card>
                            <el-card class="common-card" v-if="cardVisible[1]" :style="{zIndex: 9999}">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Sound</span>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" class="nightbutton" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard(1)" circle />
                                    </div>
                                </div>
                                <el-row v-for="(audio, index) in audioContent" :key="index">
                                    <span style="color: #fff; margin-top: 8px; display: flex; font-size: 12px;">
                                        {{ audio.label }}
                                    </span>
                                    <el-slider
                                        ref="volumeSlider"
                                        :min="0"
                                        :max="100"
                                        :step="1"
                                        :show-tooltip="false"
                                        v-model="volume[audio.prop]"
                                        :track-style="{ height: '6px', width: '70%', left: '15%' }"
                                        @change="changeVolume"
                                    >
                                        <template #start>0</template>
                                        <template #end>100</template>
                                    </el-slider>
                                </el-row>
                            </el-card>
                            <el-card class="common-card" v-if="cardVisible[2]" :style="{ backgroundColor: '#000' }">
                                <div slot="header" class="card-header">
                                    <span style="color: #fff" >Quoto</span>
                                    <div style="display: flex;">
                                        <el-button :icon="Close" type="primary" color="black" size="small" style="position: absolute;right: 10px;" @click="closeCard(2)" circle />
                                    </div>
                                </div>
                                <div style="color: #fff">
                                    <el-row justify="center">
                                        <el-button color="#000" @click="shuffleQuoto">
                                            Shuffle<el-icon  class="myicon"><Refresh /></el-icon>
                                        </el-button>
                                    </el-row>
                                    <el-row justify="center">
                                        <el-button color="#000" v-if="quotoVisible" @click="hideQuoto">
                                            Hide<el-icon  class="myicon"><Hide /></el-icon>
                                        </el-button>
                                        <el-button color="#000" v-if="!quotoVisible" @click="showQuoto">
                                            View<el-icon  class="myicon"><View /></el-icon>
                                        </el-button>
                                    </el-row>
                                </div>
                            </el-card>
                        </div></el-col>
                </el-row>
<!--                第三行用于展示名言-->
                <el-row class="quoto-row" justify="end">
                    <el-col :span="6"><div class="welcome">
                        <p style="font-size: 25px; color: white" v-if="quotoVisible">{{quotoSentence}}</p>
                    </div><div class="grid-content ep-bg-purple" /></el-col>
                </el-row>
            </el-card>
        </div>
    </div>
</template>

<script>
import Header from "../components/userHeader.vue";
import Aside from "../components/userAside.vue";
import { Delete, Edit, Search, Share, Upload, Close, FullScreen, Picture, Headset, EditPen, Timer, VideoPause, RefreshLeft, Plus, Minus, Refresh, Hide, View} from '@element-plus/icons-vue'
import {watch} from "vue";
import {leaveTime} from "@/views/Room.vue";
import FriendsModule from "@/views/FriendsModule/FriendsModule.vue";

export default {
    name: "soloStudy",
    computed: {
        Close() {
            return Close
        },
        Search() {
            return Search
        },
        FullScreen() {
            return FullScreen
        },
        Picture() {
            return Picture
        },
        Headset() {
            return Headset
        },
        EditPen() {
            return EditPen
        },
        Timer() {
            return Timer
        },
        VideoPause() {
            return VideoPause
        },
        RefreshLeft() {
            return RefreshLeft
        },
        Plus() {
            return Plus
        },
        Minus() {
            return Minus
        },
        Refresh() {
            return Refresh
        },
        Hide() {
            return Hide
        },
        View() {
            return View
        },
        formatTime() {
            const hours = Math.floor(this.timeRemaining / 3600);
            const minutes = Math.floor((this.timeRemaining % 3600) / 60);
            const seconds = Math.floor(this.timeRemaining % 60);
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        },
        initTime() {
            const setHours = Math.floor(this.duration / 3600);
            const setMinutes = Math.floor((this.duration % 3600) / 60);
            const setSeconds = Math.floor(this.duration % 60);
            return `${setHours.toString().padStart(2, '0')}:${setMinutes.toString()
                .padStart(2, '0')}:${setSeconds.toString().padStart(2, '0')}`;
        },
        quotoSentence() {
            const sentences = [
                '“No matter what people tell you, words and ideas can change the world.”',
                '“Don\'t try to become a person of importance. Try to become a person of value.”',
                '“Live as if you were to die tomorrow. Learn as if you were to live forever.”',
                '“You can be everything. You can be the infinite amount of things that people are.”',
                '“Wake up determined, go to bed satisfied.”',
                '“If you don’t like the road you’re walking, start paving another one!”',
                '“Without great solitude, no serious work is possible.”',
                '“Many of life’s failures are people who did not realize how close they were to success when they gave up.”',
                '“If people only knew how hard I’ve worked to gain my mastery, it wouldn’t seem so wonderful at all.”',
                '“There is only one way to learn... It\'s through action. Everything you need to know you have learned through your journey.”',
            ];
            return sentences[this.randomInt];
        }
    },
    components: {
        FriendsModule,
        RefreshLeft,
        VideoPause,
        Aside,
        Header,
        Delete,
        Edit,
        Search,
        Share,
        Upload,
        Close,
        FullScreen,
        Picture,
        Minus,
        Plus,
        Refresh,
        Hide,
        View,
        Headset,
        Timer
    },
    data() {
        return {
            cardVisible: [false, false, false, false],
            isFullScreen: false,
            timeRemaining: 600, // 倒计时时间（单位：秒）
            intervalId: null, // 用于保存计时器的 ID
            isPaused: false, // 是否暂停计时器
            duration: 0,
            rowVisible: true,
            quotoVisible:true,
            randomInt:0,
            volume: {
                audio1: 0,
                audio2: 0,
                audio3: 0,
                audio4: 0,
            },
            audioList: [],
            audioContent: [
                { label: 'bird sound', prop: 'audio1' },
                { label: 'rain sound', prop: 'audio2' },
                { label: 'daylight', prop: 'audio3' },
                { label: 'piano', prop: 'audio4' },
            ],
            backgroundImageUrl: require('../assets/bk7.gif'),
            urllist: [ require('../assets/bk1.gif'),
                require('../assets/bk2.gif'),
                require('../assets/bk3.jpg'),
                require('../assets/bk4.gif'),
                require('../assets/bk5.gif'),
                require('../assets/bk6.gif'),
                require('../assets/bk7.gif'),
                require('../assets/bk8.gif'),
                require('../assets/bk9.gif'),
                ],
            selectedImageIndex: 6,
        }
    },
    inject: ['isNightMode','isView'],
    methods: {
        showCard(index) {
            this.cardVisible = this.cardVisible.map((value, i) => i === index);
            //音乐选项直接播放音乐
            if(index===1){
                this.audioList = ['bird', 'rain', 'Daylight', 'Piano'].map((audioName, index) => {
                    const audio = new Audio(require(`@/assets/${audioName}.mp3`));
                    audio.loop = true;

                    // 确保音量值在0到100之间，并转换为0到1之间的范围
                    const volumeValue = Math.min(100, Math.max(0, this.volume['audio' + (index + 1)]));
                    audio.volume = volumeValue / 100;

                    audio.play();
                    return audio;
                });
            }
        },
        closeCard(index) {
            this.cardVisible[index] = false;
            if(index===1){
                this.audioList.forEach((audio, index) => {
                    const audioName = 'audio' + (index + 1);
                    const volumeValue = 0; // 将音量值设置为零
                    audio.volume = volumeValue / 100;
                });
            }
        },
        showRow() {
            this.rowVisible = false
            this.duration = 0
        },
        closeRow() {
            this.rowVisible = true
            this.timeRemaining = this.duration
        },
        showQuoto() {
            this.quotoVisible = true
        },
        hideQuoto() {
            this.quotoVisible = false
        },
        countDown() {
            this.intervalId = setInterval(() => {
                if (!this.isPaused) { // 如果没有暂停计时器，则每秒更新剩余时间
                    if (this.timeRemaining > 0) {
                        this.timeRemaining--;
                    } else {
                        clearInterval(this.intervalId);
                    }
                }
            }, 1000);
        },
        togglePause() {
            this.isPaused = !this.isPaused; // 切换 isPaused 属性的值
        },
        incrementDuration() {
            this.duration += 5 * 60;
        },
        decrementDuration() {
            if (this.duration >= 5 * 60) {
                this.duration -= 5 * 60;
            }
        },
        shuffleQuoto() {
            this.randomInt = Math.floor(Math.random() * 10);
        },
        nightModeChanged(){
            if(this.isNightMode.value) {
                const icons = document.querySelectorAll('.nightIcon');
                icons.forEach(icon => {
                    icon.style.color = '#f1f4fc';
                });
            }
            else{
                const icons = document.querySelectorAll('.nightIcon');
                icons.forEach(icon => {
                    icon.style.color = '#111828';
                });
            }
        },
        changeVolume() {
            this.audioList.forEach((audio, index) => {
                const audioName = 'audio' + (index + 1);
                // 确保音量值在0到100之间，并转换为0到1之间的范围
                const volumeValue = Math.min(100, Math.max(0, this.volume[audioName]));
                audio.volume = volumeValue / 100;
            });
        },
        changeBackground(index) {
            // 在这里可以编写逻辑来改变背景图片URL
            this.backgroundImageUrl = this.urllist[index];
        },
        simulateF11() {
            const element = document.documentElement; // 获取整个文档的根元素

            if (document.fullscreenElement) {
                // 如果当前已处于全屏状态，则退出全屏
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                }
            } else {
                // 否则，进入全屏
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                }
            }
        },
    },
    mounted() {
        this.countDown();
        leaveTime(this.isView);
        watch(
            () => this.isNightMode.value,
            (newValue, oldValue) => {
                this.nightModeChanged();
            }
        )
    },
}

</script>

<style scoped>
.container {
    position: absolute;
    right: 10px;
    width: calc(100% - 75px);
}

.box-card {
    padding: 0;
    margin-right: 5px;
    width: 100%;
    height: calc(100vh - 80px);
    display: flex;
    background-image: url(../assets/bk3.jpg);
    background-size: 100%;
    border-radius: 15px
}

.pop-card {
    font-family: 'Arial','Comic Sans MS',sans-serif;
    line-height: 1.5;
    margin-top: 5vh;
    display: flex;
}

.right-button {
    text-align: right;
}

.right-pop-card {
    display: flex;
    justify-content: flex-end;
}

.welcome {
    font-family: 'Arial','Comic Sans MS',sans-serif;
    line-height: 1.5;
    margin-top: 5vh;
    text-align: right;
}

.button-row {
    width: 88vw;
    margin-top: 0;
    margin-bottom: 0;
    padding-top: 0;
}

.content-row {
    width: 88vw;
    height: 80px;
    display: flex;
    align-items: flex-end;

}
.quoto-row {
    width: 88vw;
    margin-top: 10vh;
}
.common-button {
    height: 60px;
    line-height: 60px;
    border-radius: 10px;
    font-size: 16px;
    padding: 0 20px;
}

.time-card {
    width: 300px;
    background-color: #000000; /* 设置黑色背景色 */
    border:none;
    border-radius: 15px;
    padding: 0 !important;
}

.common-card {
    width: 265px;
    background-color: #000000; /* 设置黑色背景色 */
    border:none;
    border-radius: 15px;
    padding: 0 !important;
    opacity: 85%;
}

.time-card .card-header {
    display: flex;
    justify-content: flex-start; /* 右对齐 */
    font-size: 15px;
}

.common-card .card-header {
    display: flex;
    justify-content: flex-start; /* 右对齐 */
    font-size: 15px;
}

.card-close {
    color: #ffffff; /* 设置白色 */
    font-size: 20px;
}

.close-button {
    margin-left: 140px;
    margin-right: 0;
    margin-top: 0;
}

.pic-row {
    margin-top: 10px;
}

.image-clickable {
    border: 3px solid transparent;
    transition: border-color .3s ease;
}

.image-clickable:hover {
    border-color: white;
}

.image-container {
    position: relative;
    display: inline-block;
}

.image-container:hover .overlay {
    opacity: 1;
}

.image-container:hover .overlay::after {
    opacity: 1;
}

.el-slider {
    --el-slider-button-wrapper-size: 32px;
    --el-slider-main-bg-color: #242628;
    --el-slider-height: 2px;
    --el-slider-button-size: 12px;
}

</style>