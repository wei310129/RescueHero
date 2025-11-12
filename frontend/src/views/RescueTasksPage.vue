<template>
  <div class="tasks-container">
    <h2>目前可接的救援任務</h2>
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
          <option v-for="p in priorities" :key="p" :value="p">{{ p }}</option>
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
      <ul v-if="tasks.length > 0" class="task-list">
        <li v-for="task in tasks" :key="task.id" class="task-item">
          <div class="task-title">
            <router-link :to="`/tasks/${task.id}`">{{ task.title }}</router-link>
          </div>
          <div class="task-desc">{{ task.description }}</div>
          <div class="task-status">狀態：{{ task.status }}</div>
        </li>
      </ul>
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
.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}
@media (max-width: 600px) {
  .tasks-container {
    padding: 12px 4px;
    border-radius: 0;
    box-shadow: none;
  }
  .filters {
    flex-direction: column;
    gap: 10px;
  }
  .task-item {
    padding: 12px 0;
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
.task-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.task-item {
  padding: 18px 0;
  border-bottom: 1px solid #eee;
  width: 100%;
  box-sizing: border-box;
}
.task-title a {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1976d2;
  text-decoration: underline;
  cursor: pointer;
  word-break: break-all;
}
.task-desc {
  margin: 8px 0;
  color: #333;
}
.task-status {
  font-size: 0.95rem;
  color: #666;
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
