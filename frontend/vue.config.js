const fs = require('fs');
const { defineConfig } = require('@vue/cli-service')
// vue.config.js
module.exports = {
  devServer: {
    // HTTPS 憑證：
    https: {
      key: fs.readFileSync('./cert/localhost+1-key.pem'),
      cert: fs.readFileSync('./cert/localhost+1.pem')
    },
    port: 8080,
  }
}
