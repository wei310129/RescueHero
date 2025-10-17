// const fs = require('fs');
// const { defineConfig } = require('@vue/cli-service')
// vue.config.js
module.exports = {
  devServer: {
    // HTTPS 憑證：
    // https: {
    //   key: fs.readFileSync('./cert/localhost+1-key.pem'),
    //   cert: fs.readFileSync('./cert/localhost+1.pem')
    // },
    port: 8080,

    // 關掉 HMR（Hot Module Replacement）
    hot: false,
    // 關掉 Live Reload（自動刷新頁面）
    liveReload: false,
    // 關掉錯誤畫面 overlay
    client: {
      overlay: false,
      progress: false, // 不顯示 console 的進度條
      webSocketURL: {
        protocol: 'wss', // 你如果有 HTTPS，可留 wss；否則可改成 ws
        hostname: '0.0.0.0', // 或 localhost，也可改你的 IP
        port: 8080,
        pathname: undefined // 不指定 /ws，避免衝突
      }
    }
  }
}
