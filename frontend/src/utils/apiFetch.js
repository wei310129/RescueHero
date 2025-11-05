// apiFetch.js - fetch wrapper for auth token management

export async function apiFetch(url, options = {}) {
    // Get access token from localStorage
    const accessToken = localStorage.getItem('accessToken');
    // Clone and set headers
    const headers = new Headers(options.headers || {});
    if (accessToken) {
        headers.set('Authorization', `${accessToken}`);
    }
    // Compose fetch options
    const fetchOptions = {
        ...options,
        headers,
        credentials: 'include', // always send cookies for refresh token
    };
    const response = await fetch(url, fetchOptions);
    if (response.status === 401) { // 在後端 filter 中發現 accessToken 過期就會回傳 401 Unauthorized
        // Clear access token
        localStorage.removeItem('accessToken');
        // Call backend logout to clear refresh token cookie
        await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
    }
    return response;
}

