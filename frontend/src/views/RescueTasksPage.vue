<template>
  <div class="tasks-container">
    <h2>{{ showAcceptedTasks ? '我的救援任務' : '可接的救援任務' }}</h2>
    <div class="filters">
      <label>
        優先度：
        <select v-model="selectedPriority" class="modern-input">
          <option :value="null">全部</option>
          <option :value="1">高</option>
          <option :value="2">中高</option>
          <option :value="3">中</option>
          <option :value="4">中低</option>
          <option :value="5">低</option>
        </select>
      </label>
      <label>
        關鍵字：
        <input v-model="nameLike" type="text" class="modern-input" placeholder="任務名稱關鍵字" />
      </label>
      <button @click="fetchTasks">查詢</button>
    </div>
    <div class="actions-row">
      <div class="display-toggle">
        <button :class="{active: displayMode==='card'}" @click="setDisplayMode('card')" aria-label="區塊顯示">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="3" y="3" width="7" height="7" rx="2" fill="currentColor"/>
            <rect x="14" y="3" width="7" height="7" rx="2" fill="currentColor"/>
            <rect x="3" y="14" width="7" height="7" rx="2" fill="currentColor"/>
            <rect x="14" y="14" width="7" height="7" rx="2" fill="currentColor"/>
          </svg>
        </button>
        <button :class="{active: displayMode==='list'}" @click="setDisplayMode('list')" aria-label="清單顯示">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="6" width="16" height="2.5" rx="1.2" fill="currentColor"/>
            <rect x="4" y="11" width="16" height="2.5" rx="1.2" fill="currentColor"/>
            <rect x="4" y="16" width="16" height="2.5" rx="1.2" fill="currentColor"/>
          </svg>
        </button>
      </div>
      <button class="my-tasks-btn" :class="{'accepted': showAcceptedTasks}" @click="toggleTaskType">
        {{ showAcceptedTasks ? '可接任務' : '已接任務' }}
      </button>
    </div>
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else>
      <!-- 修改優先度顯示，1=高(紅色粗體)、2=中高、3=中、4=中低、5=低 -->
      <!-- 卡片顯示 -->
      <ul v-if="tasks.length > 0 && displayMode==='card'" class="task-list">
        <li v-for="task in tasks" :key="task.id" class="task-item card">
          <div class="card-header">
            <span class="disaster-country">{{ task.disaster?.country?.name || '-' }}</span>
            <span class="disaster-name">｜{{ task.disaster?.name || '-' }}</span>
          </div>
          <div class="task-main">
            <div class="task-name">
              <strong>
                <span v-if="task.status?.name" :class="statusColorClass(task.status?.name)">({{ task.status.name }}) </span>{{ task.name }}
              </strong>
              <span v-if="task.currentMemberCount >= task.maxMember" style="font-size:0.92em;color:#d32f2f;font-weight:400;margin-left:6px;">（已額滿）</span>
            </div>
            <div class="task-desc">{{ task.description }}</div>
          </div>
          <div class="task-info">
            <span :class="priorityClass(task.priority)">
              優先度：<span>{{ priorityLabels[task.priority-1] }}</span>
            </span>
            <span class="task-member">需求人數：{{ task.minMember }} - {{ task.maxMember }}</span>
            <span class="task-member">目前人數：{{ task.currentMemberCount }}</span>
          </div>
        </li>
      </ul>
      <!-- 清單顯示 -->
      <table v-if="tasks.length > 0 && displayMode==='list'" class="task-table">
        <thead>
          <tr>
            <th>國家</th>
            <th>災害名稱</th>
            <th>任務名稱/描述</th>
            <th>優先度</th>
            <th>需求人數</th>
            <th>目前人數</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="task in tasks" :key="task.id">
            <td>{{ task.disaster?.country?.name || '-' }}</td>
            <td>{{ task.disaster?.name || '-' }}</td>
            <td>
              <div class="task-name-list">
                <strong>
                  <span v-if="task.status?.name" :class="statusColorClass(task.status?.name)">({{ task.status.name }}) </span>{{ task.name }}
                </strong>
                <span v-if="task.currentMemberCount >= task.maxMember" style="font-size:0.92em;color:#d32f2f;font-weight:400;margin-left:6px;">（已額滿）</span>
              </div>
              <div class="task-desc-list">{{ task.description }}</div>
            </td>
            <td :class="priorityClass(task.priority)">
              <span>{{ priorityLabels[task.priority-1] }}</span>
            </td>
            <td>{{ task.minMember }} - {{ task.maxMember }}</td>
            <td>{{ task.currentMemberCount }}</td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty">目前沒有可接的任務</div>
      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">‹ 上一頁</button>
        <span>第 {{ currentPage }} 頁 / 共 {{ totalPages }} 頁</span>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一頁 ›</button>
      </div>
      <div class="size-selector">
        <label>
          每頁筆數：
          <select v-model="selectedSize" @change="changeSize">
            <option v-for="size in sizeOptions" :key="size" :value="size">{{ size }}</option>
          </select>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {apiFetch} from '@/utils/apiFetch'

const tasks = ref([])
const loading = ref(true)
const router = useRouter()

// 下拉選單資料（hardcode）
const groups = ref([
  {id: 1, name: 'A組'},
  {id: 2, name: 'B組'},
  {id: 3, name: 'C組'}
])
const disasterTypes = ref([
  {id: 1, name: '火災'},
  {id: 2, name: '水災'},
  {id: 3, name: '地震'}
])
const statuses = ref([
  {id: 1, name: '進行中'},
  {id: 2, name: '已結案'}
])
const priorities = ref([1,2,3,4,5])
const sizeOptions = ref([10, 20, 50])
const priorityLabels = ['高', '中高', '中', '中低', '低']

// 選擇的值，狀態預設為"進行中"（id:1），其他預設null
const selectedGroup = ref(null)
const selectedDisasterId = ref(null)
const selectedStatusId = ref(1)
const selectedPriority = ref(null)
const nameLike = ref('')
const selectedSize = ref(10)
const currentPage = ref(1)
const totalPages = ref(1)
const totalElements = ref(0)
const displayMode = ref('card')
const SHOW_ACCEPTED_KEY = 'rescueTasksShowAccepted'
const showAcceptedTasks = ref(false)

// Persisted key for remembering user's display preference
const DISPLAY_MODE_KEY = 'rescueTasksDisplayMode'

function setDisplayMode(mode) {
  if (!mode) return
  if (mode !== 'card' && mode !== 'list') return
  displayMode.value = mode
  try {
    if (typeof window !== 'undefined' && window.localStorage) {
      window.localStorage.setItem(DISPLAY_MODE_KEY, mode)
    }
  } catch (e) {
    // ignore localStorage errors (e.g., privacy mode)
  }
}

async function fetchTasks() {
  loading.value = true
  try {
    const requestBody = {
      priority: selectedPriority.value,
      nameLike: nameLike.value,
      page: {
        page: currentPage.value - 1,
        size: selectedSize.value
      }
    }
    Object.keys(requestBody).forEach(key => {
      if (requestBody[key] === null || requestBody[key] === '') {
        requestBody[key] = null
      }
    })
    const apiPath = showAcceptedTasks.value ? '/group-task/accepted' : '/group-task/available'
    const res = await apiFetch(apiPath, {
      method: 'POST',
      body: JSON.stringify(requestBody)
    })
    if (res.ok) {
      const result = await res.json()
      tasks.value = (result.content || []).slice().sort((a, b) => a.priority - b.priority)
      totalPages.value = result.totalPages || 1
      totalElements.value = result.totalElements || tasks.value.length
    } else {
      tasks.value = []
      totalPages.value = 1
      totalElements.value = 0
    }
  } catch (e) {
    tasks.value = []
    totalPages.value = 1
    totalElements.value = 0
  }
  loading.value = false
}

function viewTask(task) {
  router.push(`/tasks/${task.id}`)
}

function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    fetchTasks()
  }
}

function changeSize() {
  currentPage.value = 1
  fetchTasks()
}

function priorityClass(priority) {
  switch (priority) {
    case 1: return 'priority-high';
    case 2: return 'priority-midhigh';
    case 3: return 'priority-mid';
    case 4: return 'priority-midlow';
    case 5: return 'priority-low';
    default: return '';
  }
}

function goToMyTasks() {
  router.push('/my-tasks') // 根據你的路由設計調整路徑
}

function toggleTaskType() {
  showAcceptedTasks.value = !showAcceptedTasks.value
  try {
    if (typeof window !== 'undefined' && window.localStorage) {
      window.localStorage.setItem(SHOW_ACCEPTED_KEY, showAcceptedTasks.value ? '1' : '0')
    }
  } catch (e) {}
  currentPage.value = 1
  fetchTasks()
}

function statusColorClass(name) {
  switch (name) {
    case '待處理': return 'status-pending';
    case '進行中': return 'status-inprogress';
    case '已完成': return 'status-completed';
    case '已取消': return 'status-cancelled';
    default: return '';
  }
}

onMounted(() => {
  // restore persisted display mode before fetching so UI layout matches immediately
  try {
    if (typeof window !== 'undefined' && window.localStorage) {
      const savedDisplay = window.localStorage.getItem(DISPLAY_MODE_KEY)
      if (savedDisplay === 'card' || savedDisplay === 'list') {
        displayMode.value = savedDisplay
      }
        const savedAccepted = window.localStorage.getItem(SHOW_ACCEPTED_KEY)
      if (savedAccepted === '1') {
        showAcceptedTasks.value = true
      } else {
        showAcceptedTasks.value = false
      }
    }
  } catch (e) {}
  fetchTasks()
})
</script>

<style scoped>
.tasks-container {
  max-width: 700px;
  margin: 8px auto 0 auto;
  padding: 32px 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
}
.actions-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 8px;
  justify-content: flex-start;
}
.my-tasks-btn {
  background: #ff9800;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 8px 18px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
.my-tasks-btn.accepted {
  background: #388e3c;
  color: #fff;
}
.display-toggle {
  display: flex;
  gap: 12px;
  margin-bottom: 0;
}
.display-toggle button {
  padding: 6px;
  border: 1px solid #1976d2;
  background: #fff;
  color: #1976d2;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s, color 0.2s;
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.display-toggle button svg {
  display: block;
  width: 22px;
  height: 22px;
}
.display-toggle button.active {
  background: #1976d2;
  color: #fff;
}
.filters {
  display: flex;
  gap: 16px;
  margin-top: 25px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}
.task-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 18px;
  list-style: none;
  padding: 0;
  margin: 0;
}
.task-item.card {
  background: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  padding: 18px 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.card-header {
  font-size: 1.05rem;
  color: #1976d2;
  font-weight: 600;
  margin-bottom: 4px;
  word-break: break-all;
}
.task-main {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.task-name {
  font-size: 1.15rem;
  font-weight: bold;
  color: #222;
  word-break: break-all;
}
.task-desc {
  margin: 2px 0 8px 0;
  color: #888;
  font-size: 0.92rem;
  word-break: break-all;
}
.task-info {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  font-size: 0.98rem;
  color: #555;
  justify-content: center;
  text-align: center;
}
.task-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 18px;
  background: #f8f9fa;
  border-radius: 10px;
  overflow: hidden;
  font-size: 0.98rem;
}
.task-table th, .task-table td {
  padding: 10px 6px;
  border-bottom: 1px solid #e0e0e0;
  text-align: center;
  word-break: break-all;
}
.task-table th {
  background: #e3eafc;
  color: #1976d2;
  font-weight: 700;
}
.task-table tr:last-child td {
  border-bottom: none;
}
.task-table td:nth-child(3) {
  text-align: left;
}
.task-table td:nth-child(4) {
  color: #1976d2;
  font-weight: 500;
}
.task-table td:nth-child(5) {
  color: #d2691e;
}
.task-table td:nth-child(6) {
  color: #388e3c;
}
.task-name-list {
  font-size: 1.05rem;
  font-weight: bold;
  color: #222;
  word-break: break-all;
}
.task-desc-list {
  color: #888;
  font-size: 0.92rem;
  word-break: break-all;
}
.task-status {
  color: #1976d2;
  font-weight: 500;
}
.task-priority {
  color: #d2691e;
}
.task-member {
  color: #388e3c;
}
.priority-high-label {
  color: #d32f2f;
  font-weight: bold;
}
.priority-high {
  color: #d32f2f !important;
  font-weight: bold;
}
.priority-midhigh {
  color: #ff9800 !important;
  font-weight: bold;
}
.priority-mid {
  color: #ffd600 !important;
  font-weight: bold;
}
.priority-midlow {
  color: #cddc39 !important;
  font-weight: bold;
}
.priority-low {
  color: #388e3c !important;
  font-weight: bold;
}
.status-pending {
  color: #42a5f5 !important; /* 淺藍色 */
}
.status-inprogress {
  color: #d32f2f !important; /* 紅色 */
}
.status-completed {
  color: #388e3c !important; /* 綠色 */
}
.status-cancelled {
  color: #888 !important; /* 灰色 */
}
@media (max-width: 900px) {
  .task-list {
    grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  }
}
@media (max-width: 600px) {
  .task-list {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  .task-item.card {
    padding: 12px 8px;
  }
}
.filters label {
  display: flex;
  align-items: center;
  gap: 8px;
}
.filters button {
  padding: 6px 14px;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
h2 {
  margin-bottom: 12px;
  color: #1976d2;
  font-weight: 700;
  letter-spacing: 2px;
}
.loading {
  text-align: center;
  color: #888;
  font-size: 1.1rem;
}
.empty {
  text-align: center;
  color: #999;
  font-size: 1.1rem;
  margin-top: 32px;
}
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}
.size-selector {
  margin-top: 16px;
  text-align: right;
}
.modern-input {
  height: 34px;
  border-radius: 6px;
  border: 1px solid #1976d2;
  padding: 0 12px;
  font-size: 1rem;
  background: #f8f9fa;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.07);
  transition: border-color 0.2s, box-shadow 0.2s;
  outline: none;
}
.modern-input:focus,
.modern-input:hover {
  border-color: #388e3c;
  box-shadow: 0 2px 12px rgba(56, 142, 60, 0.12);
}
</style>
