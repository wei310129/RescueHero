// apiFetch.js - fetch wrapper for auth token management
export const apiContextPath = "/api";
export const stompAppPrefix = "/app";

export async function apiFetch(url, options = {}) {
    let newUrl = apiContextPath + url;
    // Get access token from localStorage
    const accessToken = localStorage.getItem('accessToken');
    // Clone and set headers
    const headers = new Headers(options.headers || {});
    headers.set('Accept', 'application/json');
    // 強制 Content-Type: application/json
    if (options.body) {
        headers.set('Content-Type', 'application/json');
    }
    if (accessToken) {
        headers.set('Authorization', `${accessToken}`);
    }
    // Compose fetch options
    const fetchOptions = {
        ...options,
        headers,
        credentials: 'include', // always send cookies for refresh token
    };
    console.log("newUrl: " + newUrl);
    let response = await fetch(newUrl, fetchOptions);
    if (response.status === 401) { // Token expired or unauthorized
        // Try to refresh token
        const refreshResponse = await fetch(apiContextPath + '/auth/refresh', {
            method: 'POST',
            credentials: 'include',
            headers: new Headers({ 'Accept': 'application/json' })
        });

        let authHeader = refreshResponse.headers.get('Authorization')
        if (!refreshResponse.ok || !authHeader) {
            localStorage.removeItem('accessToken');
            // 全域登出，讓 header UI 也同步登出
            if (typeof window.logout === 'function') {
                console.log("執行登出")
                window.logout(); // 你需在 main.js 或 store 實作 window.logout 來同步登出
            }
            // 這裡可加上導向登入頁或其他 logout 處理
            return response;
        }

        localStorage.setItem('accessToken', authHeader);
        headers.set('Authorization', authHeader);
        console.log("繼續執行原操作")
        response = await fetch(newUrl, { ...fetchOptions, headers });
    } else if (response.status === 403) {
        alert("操作被拒，權限不足")
    }
    return response;
}
