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

    // 1) 不要注入 webpack-dev-server client 腳本
    client: false,

    // 2) 不要啟動 dev server 內建的 WebSocket server
    webSocketServer: false
  }
}
