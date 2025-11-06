<template>
  <div class="tasks-container">
    <h2>目前可接的救援任務</h2>
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else>
      <ul v-if="tasks.length > 0" class="task-list">
        <li v-for="task in tasks" :key="task.id" class="task-item">
          <div class="task-title">
            <router-link :to="`/tasks/${task.id}`">{{ task.title }}</router-link>
          </div>
          <div class="task-desc">{{ task.description }}</div>
          <div class="task-location">地點：{{ task.location }}</div>
          <div class="task-status">狀態：{{ task.status }}</div>
        </li>
      </ul>
      <div v-else class="empty">目前沒有可接的任務</div>
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

async function fetchTasks() {
  loading.value = true
  try {
    const res = await apiFetch('/group-task/available')
    if (res.ok) {
      tasks.value = await res.json()
    } else {
      tasks.value = []
    }
  } catch (e) {
    tasks.value = []
  }
  loading.value = false
}

function viewTask(task) {
  router.push(`/tasks/${task.id}`)
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
}
.task-title a {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1976d2;
  text-decoration: underline;
  cursor: pointer;
}
.task-desc {
  margin: 8px 0;
  color: #333;
}
.task-location, .task-status {
  font-size: 0.95rem;
  color: #666;
}
.empty {
  text-align: center;
  color: #999;
  font-size: 1.1rem;
  margin-top: 32px;
}
</style>
