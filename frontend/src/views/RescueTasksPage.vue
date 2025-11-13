<template>
  <div class="tasks-container">
    <h2>目前可接的救援任務</h2>
    <div class="display-toggle">
      <button :class="{active: displayMode==='card'}" @click="displayMode='card'">區塊顯示</button>
      <button :class="{active: displayMode==='list'}" @click="displayMode='list'">清單顯示</button>
    </div>
    <div class="filters">
      <label>
        群組：
        <select v-model="selectedGroup">
          <option :value="null">全部</option>
          <option v-for="group in groups" :key="group.id" :value="group.id">{{ group.name }}</option>
        </select>
      </label>
      <label>
        災害類型：
        <select v-model="selectedDisasterId">
          <option :value="null">全部</option>
          <option v-for="type in disasterTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
        </select>
      </label>
      <label>
        狀態：
        <select v-model="selectedStatusId">
          <option :value="null">全部</option>
          <option v-for="status in statuses" :key="status.id" :value="status.id">{{ status.name }}</option>
        </select>
      </label>
      <label>
        優先度：
        <select v-model="selectedPriority">
          <option :value="null">全部</option>
          <option :value="1">高</option>
          <option :value="2">中高</option>
          <option :value="3">中</option>
          <option :value="4">中低</option>
          <option :value="5">低</option>
<!--          <option v-for="p in priorities" :key="p" :value="p">{{ p }}</option>-->
        </select>
      </label>
      <label>
        關鍵字：
        <input v-model="nameLike" type="text" placeholder="任務名稱關鍵字" />
      </label>
      <button @click="fetchTasks">查詢</button>
    </div>
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else>
      <ul v-if="tasks.length > 0 && displayMode==='card'" class="task-list">
        <li v-for="task in tasks" :key="task.id" class="task-item card">
          <div class="card-header">
            <span class="disaster-country">{{ task.disaster?.country?.name || '-' }}</span>
            <span class="disaster-name">｜{{ task.disaster?.name || '-' }}</span>
          </div>
          <div class="task-main">
            <div class="task-name"><strong>{{ task.name }}</strong></div>
            <div class="task-desc">{{ task.description }}</div>
          </div>
          <div class="task-info">
            <span class="task-status">狀態：{{ task.status?.name || '-' }}</span>
            <span class="task-priority">優先度：{{ task.priority }}</span>
            <span class="task-member">人數：{{ task.minMember }} - {{ task.maxMember }}</span>
          </div>
        </li>
      </ul>
      <table v-if="tasks.length > 0 && displayMode==='list'" class="task-table">
        <thead>
          <tr>
            <th>國家</th>
            <th>災害名稱</th>
            <th>任務名稱/描述</th>
            <th>狀態</th>
            <th>優先度</th>
            <th>人數</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="task in tasks" :key="task.id">
            <td>{{ task.disaster?.country?.name || '-' }}</td>
            <td>{{ task.disaster?.name || '-' }}</td>
            <td>
              <div class="task-name-list"><strong>{{ task.name }}</strong></div>
              <div class="task-desc-list">{{ task.description }}</div>
            </td>
            <td>{{ task.status?.name || '-' }}</td>
            <td>{{ task.priority }}</td>
            <td>{{ task.minMember }} - {{ task.maxMember }}</td>
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

async function fetchTasks() {
  loading.value = true
  try {
    const requestBody = {
      groupId: selectedGroup.value,
      disasterId: selectedDisasterId.value,
      // statusId: selectedStatusId.value,
      statusId: null  ,
      priority: selectedPriority.value,
      nameLike: nameLike.value,
      page: {
        page: currentPage.value - 1, // Spring page page從0開始
        size: selectedSize.value
      }
    }
    Object.keys(requestBody).forEach(key => {
      if (requestBody[key] === null || requestBody[key] === '') {
        requestBody[key] = null
      }
    })
    const res = await apiFetch('/group-task/available', {
      method: 'POST',
      body: JSON.stringify(requestBody)
    })
    if (res.ok) {
      const result = await res.json()
      tasks.value = result.content || []
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

onMounted(() => {
  fetchTasks()
})
</script>

<style scoped>
.tasks-container {
  max-width: 700px;
  margin: 80px auto 0 auto;
  padding: 32px 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
}
.display-toggle {
  display: flex;
  gap: 12px;
  margin-bottom: 18px;
}
.display-toggle button {
  padding: 6px 18px;
  border: 1px solid #1976d2;
  background: #fff;
  color: #1976d2;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s, color 0.2s;
}
.display-toggle button.active {
  background: #1976d2;
  color: #fff;
}
.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
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
  padding: 6px 16px;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
h2 {
  margin-bottom: 24px;
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
</style>
